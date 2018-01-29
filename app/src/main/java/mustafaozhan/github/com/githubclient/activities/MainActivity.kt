package mustafaozhan.github.com.githubclient.activities

import android.annotation.SuppressLint
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
import mustafaozhan.github.com.githubclient.extensions.putStringPreferences
import mustafaozhan.github.com.githubclient.utils.MyWebViewClient

class MainActivity : AppCompatActivity() {


    private var url = "https://github.com/"


    private var doubleBackToExitPressedOnce = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        mGifLayout.visibility = View.GONE

        if (getPreferences(MODE_PRIVATE).getBoolean("is_first_run", true)) {
            url = "https://github.com/login"
            getPreferences(MODE_PRIVATE).edit().putBoolean("is_first_run", false).apply()
        }

        initWebView()
        setListeners()
        webView.loadUrl(url)


    }

    private fun setListeners() {
        dashHomePage.setOnClickListener { webView.loadUrl("https://github.com/") }
        dashLogout.setOnClickListener { webView.loadUrl("https://github.com/logout") }
        dashProfile.setOnClickListener { webView.loadUrl("https://github.com/profile") }
        dashSettings.setOnClickListener { webView.loadUrl("https://github.com/settings") }
        dashSearch.setOnClickListener { webView.loadUrl("https://github.com/search") }
        mSwipeRefreshLayout.setOnRefreshListener {
            webView.loadUrl(webView.url)
            mSwipeRefreshLayout.isRefreshing = false
        }
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
