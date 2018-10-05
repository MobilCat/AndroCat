package mustafaozhan.github.com.androcat.main.activity


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_main.*
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseFragment
import mustafaozhan.github.com.androcat.base.BaseMvvmActivity
import mustafaozhan.github.com.androcat.main.fragment.MainFragment
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    companion object {
        var uri: String? = null
    }

    private var mInterstitialAd: InterstitialAd? = null
    private var scheduler: ScheduledExecutorService? = null
    private var occurs = 7
    private var adVisibility = false
    private var doubleBackToExitPressedOnce = false

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareAd()
    }

    override fun onResume() {
        super.onResume()
        ad()
        val data = this.intent.data
        if (data != null && data.isHierarchical)
            uri = this.intent.dataString
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
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else
            super.onBackPressed()
    }

    private fun ad() {
        adVisibility = true
        if (scheduler == null) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
            (scheduler as ScheduledExecutorService).scheduleAtFixedRate({
                runOnUiThread {

                    if (mInterstitialAd?.isLoaded == true && adVisibility && occurs == 7) {
                        mInterstitialAd?.show()
                        occurs = 0
                    } else
                        Log.d("TAG", "Interstitial not loaded")
                    prepareAd()
                    occurs++

                }
            }, 44, 44, TimeUnit.SECONDS)

        }
    }

    private fun prepareAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd?.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    override fun onPause() {
        super.onPause()
        scheduler?.shutdownNow()
        scheduler = null
        adVisibility = false
    }
}