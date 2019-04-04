package mustafaozhan.github.com.androcat.main.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_main.webView
import mustafaozhan.github.com.androcat.BuildConfig
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseFragment
import mustafaozhan.github.com.androcat.base.BaseMvvmActivity
import mustafaozhan.github.com.androcat.main.fragment.MainFragment
import mustafaozhan.github.com.androcat.model.RemoteConfig
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Suppress("TooManyFunctions")
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    companion object {
        var uri: String? = null
        const val HANDLER_CYCLE = 7
        const val HANDLER_SECOND: Long = 44
        const val BACK_DELAY: Long = 2000
        const val CHECK_DURATION: Long = 6
        const val REMOTE_CONFIG = "remote_config"
    }

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private var mInterstitialAd: InterstitialAd? = null
    private var scheduler: ScheduledExecutorService? = null
    private var occurs = HANDLER_CYCLE
    private var adVisibility = false
    private var doubleBackToExitPressedOnce = false

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAppUpdate()
        prepareAd()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val f = supportFragmentManager.findFragmentById(containerId)

        return if (f is MainFragment) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack()
                true
            } else
                super.onKeyUp(keyCode, event)
        } else
            super.onKeyUp(keyCode, event)
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

    private fun ad() {
        adVisibility = true
        if (scheduler == null) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
            (scheduler as ScheduledExecutorService).scheduleAtFixedRate({
                runOnUiThread {

                    if (mInterstitialAd?.isLoaded == true && adVisibility && occurs == HANDLER_CYCLE) {
                        mInterstitialAd?.show()
                        occurs = 0
                    } else {
                        Log.d("TAG", "Interstitial not loaded")
                    }
                    prepareAd()
                    occurs++
                }
            }, HANDLER_SECOND, HANDLER_SECOND, TimeUnit.SECONDS)
        }
    }

    private fun prepareAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.adUnitId = getString(R.string.interstitial_ad_id)
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun checkAppUpdate() {

        val defaultMap = HashMap<String, Any>()
        defaultMap[REMOTE_CONFIG] = RemoteConfig(
            getString(R.string.remote_config_title),
            getString(R.string.remote_config_description),
            getString(R.string.url_androcat)
        )

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings
                .Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        )

        firebaseRemoteConfig.setDefaults(defaultMap)

        firebaseRemoteConfig
            .fetch(
                if (BuildConfig.DEBUG)
                    0
                else
                    TimeUnit.HOURS.toSeconds(CHECK_DURATION)
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig.activateFetched()

                    val remoteConfigStr =
                        if (TextUtils.isEmpty(firebaseRemoteConfig.getString(REMOTE_CONFIG)))
                            defaultMap[REMOTE_CONFIG] as String
                        else
                            firebaseRemoteConfig.getString(REMOTE_CONFIG)

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
                }
            }
    }

    override fun onPause() {
        super.onPause()
        scheduler?.shutdownNow()
        scheduler = null
        adVisibility = false
    }

    override fun onResume() {
        super.onResume()
        ad()
        val data = this.intent.data
        if (data != null && data.isHierarchical)
            uri = this.intent.dataString
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        supportFragmentManager?.findFragmentByTag(MainFragment.TAG)?.let {
            if (it.isVisible) {
                it.onActivityResult(requestCode, resultCode, intent)
            }
        }
    }
}