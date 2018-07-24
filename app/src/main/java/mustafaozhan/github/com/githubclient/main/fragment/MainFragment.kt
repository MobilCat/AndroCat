package mustafaozhan.github.com.githubclient.main.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_main.*
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.githubclient.R
import mustafaozhan.github.com.githubclient.base.BaseMvvmFragment
import mustafaozhan.github.com.githubclient.settings.SettingsFragment
import mustafaozhan.github.com.githubclient.tools.MyWebViewClient

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        private const val ARGS_SHOW_ON_GITHUB = "ARGS_SHOW_ON_GITHUB"
        fun newInstance(showOnGitHub: Boolean = false): MainFragment {
            val args = Bundle()
            args.putBoolean(ARGS_SHOW_ON_GITHUB, showOnGitHub)
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var url = "https://github.com/login"

    private var quickActionProfile: QuickAction? = null
    private var quickActionExplorer: QuickAction? = null

    private var mInterstitialAd: InterstitialAd? = null

    private var scheduler: ScheduledExecutorService? = null

    private var adVisibility = false

    private var occurs = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDash()
        initWebView()
        setUi()
        prepareAd()
        if (arguments?.getBoolean(ARGS_SHOW_ON_GITHUB) == true)
            webView.loadUrl("https://github.com/mustafaozhan/GitHubClient")

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
                if (mInterstitialAd?.isLoaded == true && adVisibility && occurs == 5) {
                    mInterstitialAd?.show()
                    occurs = 0
                } else
                    Log.d("TAG", "Interstitial not loaded")
                prepareAd()
                occurs++
            }, 48, 48, TimeUnit.SECONDS)

        }
    }

    private fun prepareAd() {

        mInterstitialAd = InterstitialAd(context)
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

        viewModel.initUsername()
        if (viewModel.userName != resources.getString(R.string.missUsername))
            url = "https://github.com"

        context?.let {
            quickActionExplorer = QuickAction(it, QuickAction.VERTICAL)
            quickActionExplorer?.apply {
                setColorRes(R.color.colorGitHubDash)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                val searchItem = ActionItem(1, "Search", R.drawable.search_icon)
                val marketPlaceItem = ActionItem(2, "Market Place", R.drawable.ic_shopping_cart_black_24dp)
                val trendsItem = ActionItem(3, "Trends", R.drawable.ic_trending_up_black_24dp)
                val newGistItem = ActionItem(4, "New Gist", R.drawable.ic_code_black_24dp)
                val newRepoItem = ActionItem(5, "New Repository", R.drawable.new_repo)
                addActionItem(searchItem, marketPlaceItem, trendsItem, newGistItem, newRepoItem)
            }

            quickActionProfile = QuickAction(it, QuickAction.VERTICAL)
            quickActionProfile?.apply {
                setColorRes(R.color.colorGitHubDash)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                val starItem = ActionItem(1, "Starts", R.drawable.ic_star_black_24dp)
                val gistItem = ActionItem(2, "Gists", R.drawable.ic_code_black_24dp)
                val notificationsItem = ActionItem(3, "Notifications", R.drawable.notifications)
                val applicationSettingsItem = ActionItem(4, "App Settings", R.drawable.ic_settings_black_24dp)
                val userSettings = ActionItem(5, "User Settings", R.drawable.user_settings)
                val logOutItem = ActionItem(6, "Log out", R.drawable.logout_icon)
                val loginItem = ActionItem(7, "Log in", R.drawable.login_icon)
                val userItem = ActionItem(8, "Profile", R.drawable.user)
                quickActionProfile!!.addActionItem(starItem, gistItem, notificationsItem, applicationSettingsItem, userSettings, logOutItem, loginItem, userItem)
            }
        }
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
                R.id.navigation_find -> quickActionExplorer!!.show(mBottomNavigationView.getIconAt(3))
                R.id.navigation_feed -> {
                    if (viewModel.userName == resources.getString(R.string.username))
                        webView.loadUrl("https://github.com/login")
                    else
                        webView.loadUrl("https://github.com")
                }
                R.id.navigation_pull_request -> webView.loadUrl("https://github.com/pulls")
                R.id.navigation_Issues -> webView.loadUrl("https://github.com/issues")
            }
            true
        }
        quickActionProfile!!.setOnActionItemClickListener {
            when (it.actionId) {
                1 -> {
                    if (viewModel.userName == resources.getString(R.string.missUsername)) {
                        Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show()
                        replaceFragment(SettingsFragment.newInstance(), true)
                    } else
                        webView.loadUrl("https://github.com/" + viewModel.userName + "?tab=stars")
                }
                2 -> {
                    if (viewModel.userName == resources.getString(R.string.missUsername)) {
                        Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show()
                        replaceFragment(SettingsFragment.newInstance(), true)
                    } else
                        webView.loadUrl("https://gist.github.com/" + viewModel.userName)
                }
                3 -> webView.loadUrl("https://github.com/notifications")
                4 -> replaceFragment(SettingsFragment.newInstance(), true)
                5 -> webView.loadUrl("https://github.com/settings")
                6 -> webView.loadUrl("https://github.com/logout")
                7 -> webView.loadUrl("https://gist.github.com/login")
                8 -> {
                    if (viewModel.userName == resources.getString(R.string.missUsername)) {
                        Toast.makeText(context, "Please enter your username", Toast.LENGTH_SHORT).show()
                        replaceFragment(SettingsFragment.newInstance(), true)
                    } else
                        webView.loadUrl("https://github.com/" + viewModel.userName)
                }

            }

        }
        quickActionExplorer!!.setOnActionItemClickListener {
            when (it.actionId) {
                1 -> webView.loadUrl("https://github.com/search")
                2 -> webView.loadUrl("https://github.com/marketplace")
                3 -> webView.loadUrl("https://github.com/trending")
                4 -> webView.loadUrl("https://gist.github.com/")
                5 -> webView.loadUrl("https://github.com/new")

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


    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main


}