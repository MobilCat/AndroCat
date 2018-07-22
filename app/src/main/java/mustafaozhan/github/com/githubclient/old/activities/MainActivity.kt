package mustafaozhan.github.com.githubclient.old.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.old.extensions.getStringPreferences
import mustafaozhan.github.com.githubclient.old.utils.MyWebViewClient
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var url = "https://github.com/login"
    private var doubleBackToExitPressedOnce = false
    private var quickActionProfile: QuickAction? = null
    private var quickActionFind: QuickAction? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var scheduler: ScheduledExecutorService? = null
    private var adVisibility = false
    private var occurs = 5

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Fabric.with(this, Crashlytics())
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        setContentView(R.layout.activity_main)
        mGifLayout.visibility = View.GONE

        init()
        setDash()
        initWebView()
        setUi()
        prepareAd()

    }

    override fun onResume() {
        super.onResume()
        ad()

    }

    private fun ad() {
        adVisibility = true
        if (scheduler == null) {
            scheduler = Executors.newSingleThreadScheduledExecutor()
            (scheduler as ScheduledExecutorService).scheduleAtFixedRate({
                runOnUiThread {

                    if (mInterstitialAd?.isLoaded == true && adVisibility && occurs == 5) {
                        mInterstitialAd?.show()
                        occurs = 0
                    } else
                        Log.d("TAG", "Interstitial not loaded")
                    prepareAd()
                    occurs++

                }
            }, 48, 48, TimeUnit.SECONDS)

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

    private fun init() {


        quickActionFind = QuickAction(this, QuickAction.VERTICAL)
        quickActionFind!!.setColorRes(R.color.colorGitHubDash)
        quickActionFind!!.setTextColorRes(R.color.white)
        quickActionFind!!.setEnabledDivider(false)
        val searchItem = ActionItem(1, "Search", R.drawable.search_icon)
        val marketPlaceItem = ActionItem(2, "Market Place", R.drawable.ic_shopping_cart_black_24dp)
        val exploreItem = ActionItem(3, "Trends", R.drawable.ic_trending_up_black_24dp)
        quickActionFind!!.addActionItem(searchItem, marketPlaceItem, exploreItem)

        quickActionProfile = QuickAction(this, QuickAction.VERTICAL)
        quickActionProfile!!.setColorRes(R.color.colorGitHubDash)
        quickActionProfile!!.setTextColorRes(R.color.white)
        quickActionProfile!!.setEnabledDivider(false)
        val starItem = ActionItem(1, "Starts", R.drawable.ic_star_black_24dp)
        val notificationsItem = ActionItem(2, "Notifications", R.drawable.notifications)
        val applicationSettingsItem = ActionItem(3, "App Settings", R.drawable.ic_settings_black_24dp)
        val userSettings = ActionItem(4, "User Settings", R.drawable.user_settings)
        val logOutItem = ActionItem(5, "Log out", R.drawable.logout_icon)
        val userItem = ActionItem(6, "Profile", R.drawable.user)
        quickActionProfile!!.addActionItem(starItem, notificationsItem, applicationSettingsItem, userSettings, logOutItem, userItem)

    }

    private fun setUi() {
        webView.loadUrl(url)
        mSwipeRefreshLayout.setOnRefreshListener {
            webView.loadUrl(webView.url)
            mSwipeRefreshLayout.isRefreshing = false
        }
        mBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_user -> quickActionProfile!!.show(mBottomNavigationView.getIconAt(4))
                R.id.navigation_find -> quickActionFind!!.show(mBottomNavigationView.getIconAt(3))
                R.id.navigation_feed -> webView.loadUrl("https://github.com/login")
                R.id.navigation_pull_request -> webView.loadUrl("https://github.com/pulls")
                R.id.navigation_Issues -> webView.loadUrl("https://github.com/issues")
            }
            true
        }
        quickActionProfile!!.setOnActionItemClickListener {
            when (it.actionId) {
                1 -> {
                    if (getStringPreferences(applicationContext, "username",
                                    resources.getString(R.string.missUsername)) == resources.getString(R.string.missUsername)) {
                        Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    } else
                        webView.loadUrl("https://github.com/"
                                + getStringPreferences(applicationContext, "username",
                                resources.getString(R.string.missUsername))
                                + "?tab=stars")
                }
                2 -> webView.loadUrl("https://github.com/notifications")
                3 -> startActivity(Intent(applicationContext, SettingsActivity::class.java))
                4 -> webView.loadUrl("https://github.com/settings")
                5 -> webView.loadUrl("https://github.com/logout")
                6 -> {
                    if (getStringPreferences(applicationContext, "username",
                                    resources.getString(R.string.missUsername)) == resources.getString(R.string.missUsername)) {
                        Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    } else
                        webView.loadUrl("https://github.com/"
                                + getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)))
                }

            }

        }
        quickActionFind!!.setOnActionItemClickListener {
            when (it.actionId) {
                1 -> webView.loadUrl("https://github.com/search")
                2 -> webView.loadUrl("https://github.com/marketplace")
                3 -> webView.loadUrl("https://github.com/trending")
            }
        }
    }

    private fun setDash() {
        mBottomNavigationView.inflateMenu(R.menu.bnvm_dash)
        mBottomNavigationView.enableAnimation(false)
        mBottomNavigationView.enableItemShiftingMode(false)
        mBottomNavigationView.enableShiftingMode(false)
        mBottomNavigationView.enableAnimation(false)
        mBottomNavigationView.setTextSize(12.0f)
        mBottomNavigationView.setIconsMarginTop(10)
        mBottomNavigationView.setIconSize(30.0F, 30.0F)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.webViewClient = MyWebViewClient(mGifLayout)
        var newUserAgent: String? = webView.settings.userAgentString
        try {
            val ua = webView.settings.userAgentString
            val androidOSString = webView.settings.userAgentString.substring(ua.indexOf("("), ua.indexOf(")") + 1)
            newUserAgent = webView.settings.userAgentString.replace(androidOSString, "(X11; Linux x86_64)")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        webView.settings.userAgentString = newUserAgent
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        webView.settings.textZoom = 150
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            true
        } else
            super.onKeyUp(keyCode, event)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }


}
