package mustafaozhan.github.com.androcat.main.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import androidx.annotation.NonNull
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_main.web_view
import mustafaozhan.github.com.androcat.BuildConfig
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseFragment
import mustafaozhan.github.com.androcat.base.BaseMvvmActivity
import mustafaozhan.github.com.androcat.main.fragment.MainFragment
import mustafaozhan.github.com.androcat.model.RemoteConfig
import java.util.concurrent.TimeUnit

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Suppress("TooManyFunctions")
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    companion object {
        var uri: String? = null
        const val BACK_DELAY: Long = 2000
        const val CHECK_DURATION: Long = 6
        const val CHECK_INTERVAL: Long = 4200
        const val REMOTE_CONFIG = "remote_config"
        const val AD_INITIAL_DELAY: Long = 50
        const val AD_PERIOD: Long = 250
    }

    private lateinit var rewardedAd: RewardedAd
    private lateinit var adObservableInterval: Disposable
    private lateinit var mInterstitialAd: InterstitialAd

    private var doubleBackToExitPressedOnce = false
    private var adVisibility = false
    private var firstShow = true

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRewardedAd()
        checkAppUpdate()
        prepareAd()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val f = supportFragmentManager.findFragmentById(containerId)

        return if (f is MainFragment) {
            if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {
                web_view.goBack()
                true
            } else {
                super.onKeyUp(keyCode, event)
            }
        } else {
            super.onKeyUp(keyCode, event)
        }
    }

    override fun onBackPressed() {

        val f = supportFragmentManager.findFragmentById(containerId)
        if (f is MainFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            snacky("Please click BACK again to exit")
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, BACK_DELAY)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadRewardedAd() {
        rewardedAd = RewardedAd(this, getString(R.string.rewarded_ad_unit_id))
        rewardedAd.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() = Unit
            override fun onRewardedAdFailedToLoad(errorCode: Int) = Unit
        })
    }

    private fun ad() {
        adVisibility = true
        adObservableInterval = Observable.interval(AD_INITIAL_DELAY, AD_PERIOD, TimeUnit.SECONDS)
            .debounce(0, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { count ->
                if (mInterstitialAd.isLoaded && adVisibility && viewModel.isRewardExpired()) {
                    if (firstShow) {
                        mInterstitialAd.show()
                        firstShow = false
                    } else if (count > 0) {
                        mInterstitialAd.show()
                        showRewardedAdDialog()
                    }
                } else {
                    prepareAd()
                }
            }.doOnError(::logException)
            .subscribe()
    }

    internal fun showRewardedAdDialog() {
        showDialog(
            getString(R.string.remove_ads),
            getString(R.string.remove_ads_text),
            getString(R.string.watch)
        ) {
            showRewardedAd()
        }
    }

    private fun showRewardedAd() {
        if (rewardedAd.isLoaded) {
            rewardedAd.show(this, object : RewardedAdCallback() {
                override fun onRewardedAdOpened() = Unit
                override fun onRewardedAdClosed() = loadRewardedAd()
                override fun onRewardedAdFailedToShow(errorCode: Int) = loadRewardedAd()
                override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                    viewModel.updateAdFreeActivation()
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
            })
        }
    }

    private fun prepareAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.interstitial_ad_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun checkAppUpdate() {

        val defaultMap = HashMap<String, Any>()
        defaultMap[REMOTE_CONFIG] = RemoteConfig(
            getString(R.string.remote_config_title),
            getString(R.string.remote_config_description),
            getString(R.string.url_androcat)
        )

        FirebaseRemoteConfig
            .getInstance().apply {
                setConfigSettingsAsync(
                    FirebaseRemoteConfigSettings
                        .Builder()
                        .setMinimumFetchIntervalInSeconds(CHECK_INTERVAL)
                        .build()
                )
                setDefaults(defaultMap)
                fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(CHECK_DURATION))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            activate()
                            if (TextUtils.isEmpty(getString(REMOTE_CONFIG))) {
                                defaultMap[REMOTE_CONFIG] as? String
                            } else {
                                getString(REMOTE_CONFIG)
                            }?.let { showUpdateDialog(it) }
                        }
                    }
            }
    }

    private fun showUpdateDialog(remoteConfigStr: String) {
        try {
            Gson().fromJson(
                remoteConfigStr,
                RemoteConfig::class.java
            ).apply {
                val isCancelable = forceVersion <= BuildConfig.VERSION_CODE

                if (latestVersion > BuildConfig.VERSION_CODE) {
                    showDialog(title, description, getString(R.string.update), isCancelable) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)))
                    }
                }
            }
        } catch (e: JsonSyntaxException) {
            logException(e)
        }
    }

    override fun onPause() {
        super.onPause()
        adObservableInterval.dispose()
        adVisibility = false
    }

    override fun onResume() {
        super.onResume()
        ad()
        val data = this.intent.data
        if (data != null && data.isHierarchical) {
            uri = this.intent.dataString
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        supportFragmentManager?.findFragmentByTag(MainFragment.TAG)?.let { mainFragment ->
            if (mainFragment.isVisible) {
                mainFragment.onActivityResult(requestCode, resultCode, intent)
            }
        }
    }
}
