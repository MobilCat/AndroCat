package mustafaozhan.github.com.githubclient.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide.init
import kotlinx.android.synthetic.main.activity_main.*
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.extensions.getStringPreferences
import mustafaozhan.github.com.githubclient.utils.MyWebViewClient
import me.piruin.quickaction.ActionItem


class MainActivity : AppCompatActivity() {

    private var url = "https://github.com/login"
    private var doubleBackToExitPressedOnce = false
    private val quickActionProfile = QuickAction(this, QuickAction.HORIZONTAL)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        mGifLayout.visibility = View.GONE

        init()
        setDash()
        initWebView()
        setUi()

    }

    private fun init() {
        quickActionProfile.setColorRes(R.color.colorGitHubDash)
        quickActionProfile.setTextColorRes(R.color.white)
        val starItem = ActionItem(3, "Starts", R.drawable.ic_star_black_24dp)
        val settingsItem = ActionItem(4, "Settings", R.drawable.ic_settings_black_24dp)
        val logOutItem = ActionItem(5, "Log out", R.drawable.logout_icon)
        val userItem = ActionItem(6, "Profile", R.drawable.user)
        quickActionProfile.addActionItem(starItem, settingsItem, logOutItem, userItem)
    }

    private fun setUi() {
        mSwipeRefreshLayout.setOnRefreshListener {
            webView.loadUrl(webView.url)
            mSwipeRefreshLayout.isRefreshing = false
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_user -> quickActionProfile.show(mBottomNavigationView.getIconAt(4))
                R.id.navigation_feed -> webView.loadUrl("https://github.com/login")
                R.id.navigation_pull_request -> webView.loadUrl("https://github.com/pulls")
                R.id.navigation_Issues -> webView.loadUrl("https://github.com/issues")
                R.id.navigation_search -> webView.loadUrl("https://github.com/search")
                R.id.navigation_notification -> webView.loadUrl("https://github.com/notifications")
                R.id.navigation_marketplace -> webView.loadUrl("https://github.com/marketplace")
                R.id.navigation_explore -> webView.loadUrl("https://github.com/explore")
                R.id.navigation_stars -> {
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
                R.id.navigation_settings -> {
                    val items = arrayOf("Application Settings", "GitHub Settings")
                    val builder = AlertDialog.Builder(this)
                    builder.setItems(items, { _, item ->
                        // Do something with the selection
                        if (item == 0)
                            startActivity(Intent(applicationContext, SettingsActivity::class.java))
                        else if (item == 1)
                            webView.loadUrl("https://github.com/settings")
                    })
                    val alert = builder.create()
                    alert.show()
                }
                R.id.navigation_logout -> webView.loadUrl("https://github.com/logout")
                R.id.navigation_profile -> {
                    if (getStringPreferences(applicationContext, "username",
                                    resources.getString(R.string.missUsername)) == resources.getString(R.string.missUsername)) {
                        Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    } else
                        webView.loadUrl("https://github.com/"
                                + getStringPreferences(applicationContext, "username", resources.getString(R.string.missUsername)))
                }
            }
            true
        }
        webView.loadUrl(url)
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
