package mustafaozhan.github.com.githubclient.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.extensions.getStringPreferences
import mustafaozhan.github.com.githubclient.helpers.BottomNavigationViewHelper
import mustafaozhan.github.com.githubclient.utils.MyWebViewClient


class MainActivity : AppCompatActivity() {


    private var url = "https://github.com/login"


    private var doubleBackToExitPressedOnce = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        mGifLayout.visibility = View.GONE


        initWebView()
        mSwipeRefreshLayout.setOnRefreshListener {
            webView.loadUrl(webView.url)
            mSwipeRefreshLayout.isRefreshing = false
        }
        changeLayout("dash")
//        mBottomNavigationView.menu.getItem(4).setIcon(resources.getDrawable(R.drawable.octocat_dash))
        mBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_back -> {
                    changeLayout("dash")
                    true
                }
                R.id.navigation_find -> {
                    changeLayout("find")
                    true
                }
                R.id.navigation_user -> {
                    changeLayout("user")
                    true
                }
                R.id.navigation_feed -> {
                    webView.loadUrl("https://github.com/login")
                    true
                }

                R.id.navigation_pull_request -> {
                    webView.loadUrl("https://github.com/pulls")
                    true
                }
                R.id.navigation_Issues -> {
                    webView.loadUrl("https://github.com/issues")
                    true
                }
                R.id.navigation_search -> {
                    webView.loadUrl("https://github.com/search")
                    true
                }
                R.id.navigation_marketplace -> {
                    webView.loadUrl("https://github.com/marketplace")
                    true
                }
                R.id.navigation_explore -> {
                    webView.loadUrl("https://github.com/explore")
                    true
                }
                R.id.navigation_stars -> {
                    if (getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)) == resources.getString(R.string.missUsername)) {
                        Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    } else
                        webView.loadUrl("https://github.com/" + getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)) + "?tab=stars")
                    true
                }
                R.id.navigation_github_settings -> {
                    webView.loadUrl("https://github.com/settings")
                    true
                }
                R.id.navigation_app_settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    if (getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)) == resources.getString(R.string.missUsername)) {
                        Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    } else
                        webView.loadUrl("https://github.com/" + getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)))
                    true
                }
                else -> false

            }
        }
        webView.loadUrl(url)


    }

    private fun changeLayout(s: String) {
        mBottomNavigationView.menu.clear()
        when (s) {
            "dash" -> mBottomNavigationView.inflateMenu(R.menu.bnvm_dash)
            "find" -> mBottomNavigationView.inflateMenu(R.menu.bnvm_find)
            "user" -> mBottomNavigationView.inflateMenu(R.menu.bnvm_user)
        }

        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView)
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
