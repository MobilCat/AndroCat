package mustafaozhan.github.com.androcat.main.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.github.jorgecastillo.FillableLoader
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.jakewharton.rxbinding2.widget.textChanges
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import im.delight.android.webview.AdvancedWebView
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_main.fillableLoaderLayout
import kotlinx.android.synthetic.main.fragment_main.layout_find_in_page
import kotlinx.android.synthetic.main.fragment_main.mBottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.mSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_main.newsFeedFab
import kotlinx.android.synthetic.main.fragment_main.webView
import kotlinx.android.synthetic.main.layout_fillable_loader.fillableLoader
import kotlinx.android.synthetic.main.layout_fillable_loader.fillableLoaderDarkMode
import kotlinx.android.synthetic.main.layout_find_in_page.eTxtSearch
import kotlinx.android.synthetic.main.layout_find_in_page.searchDismissButton
import kotlinx.android.synthetic.main.layout_find_in_page.searchNextButton
import kotlinx.android.synthetic.main.layout_find_in_page.searchPreviousButton
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.extensions.hideKeyboard
import mustafaozhan.github.com.androcat.extensions.isValidUsername
import mustafaozhan.github.com.androcat.extensions.remove
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.extensions.setVisibleWithAnimation
import mustafaozhan.github.com.androcat.extensions.showKeyboard
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.settings.SettingsFragment
import mustafaozhan.github.com.androcat.tools.JsScrip

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Suppress("TooManyFunctions", "MagicNumber")
class MainFragment : BaseMvvmFragment<MainFragmentViewModel>(), AdvancedWebView.Listener {
    companion object {
        private const val ARGS_OPEN_URL = "ARGS_OPEN_URL"
        const val TEXT_SIZE_SMALL = 100
        const val TEXT_SIZE_MEDIUM = 124
        const val TEXT_SIZE_LARGE = 150
        var TAG: String = MainFragment::class.java.simpleName

        fun newInstance(url: String): MainFragment {
            val args = Bundle()
            args.putString(ARGS_OPEN_URL, url)
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance() = MainFragment()
    }

    private lateinit var quickActionExplore: QuickAction
    private lateinit var quickActionStack: QuickAction
    private lateinit var quickActionProduction: QuickAction
    private lateinit var quickActionProfile: QuickAction

    private lateinit var baseUrl: String

    private var loader: FillableLoader? = null

    private var logoutCount = 0
    private var loginCount = 0
    private var isAnimating = false
    private var userName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDash()
        initWebView()
        setListeners()
        setActionListeners()

        arguments?.getString(ARGS_OPEN_URL)?.let { url ->
            loadUrlWithAnimation(url)
            arguments?.clear()
        }
    }

    @Suppress("LongMethod", "ComplexMethod")
    private fun init() {
        fillableLoader.setSvgPath(getString(R.string.androcat_svg_path))
        fillableLoaderDarkMode.setSvgPath(getString(R.string.androcat_svg_path))
        eTxtSearch.background.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        newsFeedFab.bringToFront()

        baseUrl = if (viewModel.isLoggedIn() == true) {
            setProfileActions(true)
            getString(R.string.url_github)
        } else {
            setProfileActions(false)
            getString(R.string.url_login)
        }

        viewModel.loadSettings().darkMode?.let { darkMode(it) }

        loadUrlWithAnimation(baseUrl)

        setProfileActions(false)

        context?.let { ctx ->
            quickActionExplore = QuickAction(ctx, QuickAction.VERTICAL)
            quickActionExplore.apply {
                setColorRes(R.color.colorPrimary)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                addActionItem(
                    ActionItem(6, getString(R.string.trends), R.drawable.ic_trends),
                    ActionItem(4, getString(R.string.search_in_github), R.drawable.ic_search),
                    ActionItem(5, getString(R.string.find_in_page), R.drawable.ic_find_in_page),
                    ActionItem(3, getString(R.string.dark_mode), R.drawable.ic_dark_mode),
                    ActionItem(2, getString(R.string.forward), R.drawable.ic_forward),
                    ActionItem(1, getString(R.string.back), R.drawable.ic_back)
                )
            }
            quickActionStack = QuickAction(ctx, QuickAction.VERTICAL)
            quickActionStack.apply {
                setColorRes(R.color.colorPrimary)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                addActionItem(
                    ActionItem(4, getString(R.string.gists), R.drawable.ic_gist),
                    ActionItem(3, getString(R.string.repositories), R.drawable.ic_repository),
                    ActionItem(2, getString(R.string.stars), R.drawable.ic_stars),
                    ActionItem(1, getString(R.string.notifications), R.drawable.ic_notifications)
                )
            }
            quickActionProduction = QuickAction(ctx, QuickAction.VERTICAL)
            quickActionProduction.apply {
                setColorRes(R.color.colorPrimary)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                addActionItem(
                    ActionItem(5, getString(R.string.new_gist), R.drawable.ic_gist),
                    ActionItem(4, getString(R.string.new_repository), R.drawable.ic_repository),
                    ActionItem(3, getString(R.string.projects), R.drawable.ic_projects),
                    ActionItem(2, getString(R.string.pull_requests), R.drawable.ic_pull_request),
                    ActionItem(1, getString(R.string.issues), R.drawable.ic_issue)
                )
            }
        }
    }

    private fun setListeners() {
        newsFeedFab.setOnClickListener { loadUrlWithAnimation(baseUrl) }
        webView?.setListener(getBaseActivity(), this)

        mSwipeRefreshLayout.setOnRefreshListener {
            loadUrlWithAnimation(webView?.url)
            mSwipeRefreshLayout.isRefreshing = false
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nv_explorer -> quickActionExplore.show(mBottomNavigationView.getIconAt(0))
                R.id.nv_stacks -> quickActionStack.show(mBottomNavigationView.getIconAt(1))
                R.id.nv_production -> quickActionProduction.show(mBottomNavigationView.getIconAt(3))
                R.id.nv_profile -> quickActionProfile.show(mBottomNavigationView.getIconAt(4))
            }
            true
        }

        eTxtSearch
            .textChanges()
            .subscribe { txt ->
                webView?.findAllAsync(txt.toString())
            }.addTo(compositeDisposable)

        searchNextButton.setOnClickListener {
            webView?.findNext(true)
            it.hideKeyboard()
        }
        searchPreviousButton.setOnClickListener {
            webView?.findNext(false)
            it.hideKeyboard()
        }
        searchDismissButton.setOnClickListener {
            eTxtSearch.setText("")
            layout_find_in_page.setVisibleWithAnimation(false)
            layout_find_in_page.hideKeyboard()
        }
    }

    @Suppress("ComplexMethod")
    private fun setActionListeners() {
        quickActionExplore.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> webView?.goBack()
                2 -> webView?.goForward()
                3 -> viewModel.loadSettings().darkMode?.let { darkMode(!it, true) }
                4 -> loadUrlWithAnimation(getString(R.string.url_search))
                5 -> {
                    layout_find_in_page.setVisibleWithAnimation(true)
                    eTxtSearch.showKeyboard()
                }
                6 -> loadUrlWithAnimation(getString(R.string.url_trending))
            }
        }
        quickActionStack.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> loadIfUserNameSet(getString(R.string.url_notifications))
                2 -> loadIfUserNameSet(getString(R.string.url_github) + viewModel.getUserName() + "?tab=stars")
                3 -> loadIfUserNameSet(getString(R.string.url_github) + viewModel.getUserName() + "?tab=repositories")
                4 -> loadIfUserNameSet(getString(R.string.url_gist) + viewModel.getUserName())
            }
        }
        quickActionProduction.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> loadIfUserNameSet(getString(R.string.url_issues))
                2 -> loadIfUserNameSet(getString(R.string.url_pulls))
                3 -> loadIfUserNameSet(getString(R.string.url_github) + viewModel.getUserName() + "?tab=projects")
                4 -> loadIfUserNameSet(getString(R.string.url_new))
                5 -> loadIfUserNameSet(getString(R.string.url_gist))
            }
        }
    }

    private fun setDash() = mBottomNavigationView.apply {
        inflateMenu(R.menu.bnvm_dash)
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        enableAnimation(false)
        setTextSize(10.0f)
        setIconsMarginTop(12)
        setIconSize(30.0F, 30.0F)
        getBottomNavigationItemView(2).isClickable = false

        for (i in 0..itemCount) {
            getLargeLabelAt(i)?.setPadding(0, 0, 0, 0)
            getBottomNavigationItemView(i)?.background = null
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() = webView?.apply {
        setBackgroundColor(Color.parseColor("#FFFFFF"))
        settings.apply {
            setDesktopMode(true)
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            domStorageEnabled = true
            databaseEnabled = true
        }
    }

    private fun loadIfUserNameSet(url: String) =
        viewModel.getUserName().let { username ->
            if (username.isValidUsername()) {
                if (viewModel.isLoggedIn() == false) {
                    baseUrl = getString(R.string.url_login)
                    loadUrlWithAnimation(baseUrl)
                } else {
                    // if user name valid load url anyway
                    loadUrlWithAnimation(url)
                }
            } else {
                if (viewModel.isLoggedIn() == false) {
                    baseUrl = getString(R.string.url_login)
                    loadUrlWithAnimation(baseUrl)
                } else {
                    snacky(getString(R.string.missUsername), getString(R.string.enter)) {
                        replaceFragment(SettingsFragment.newInstance(), true)
                    }
                }
            }
        }

    private fun loadingView(show: Boolean) =
        if (show and !isAnimating) {
            isAnimating = true
            fillableLoaderLayout?.setVisibleWithAnimation(true)
            loader?.visibility = View.VISIBLE
            fillableLoader?.start()
            fillableLoaderDarkMode?.start()
        } else {
            fillableLoaderLayout?.setVisibleWithAnimation(false)
            fillableLoader?.visibility = View.GONE
            fillableLoaderDarkMode?.visibility = View.GONE
            fillableLoader?.reset()
            fillableLoaderDarkMode?.reset()
            isAnimating = false
        }

    private fun darkMode(darkMode: Boolean, changeSettings: Boolean = false) {
        setDarkMode(darkMode)
        webView?.runScript(JsScrip.getDarkMode(darkMode))
        if (!darkMode) {
            webView?.reload()
        }
        if (changeSettings) {
            viewModel.updateSetting(darkMode = darkMode)
        }
    }

    private fun setDarkMode(darkMode: Boolean) = context?.let { ctx ->
        loader = if (darkMode) {
            fillableLoaderLayout?.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimaryDark))
            fillableLoader?.visibility = View.GONE
            fillableLoaderDarkMode
        } else {
            fillableLoaderLayout?.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white))
            fillableLoaderDarkMode?.visibility = View.GONE
            fillableLoader
        }
    }

    private fun loadUrlWithAnimation(urlToLoad: String?) = urlToLoad?.let { url ->
        loadingView(true)
        webView?.loadUrl(url)
    }

    override fun onPageStarted(url: String, favicon: Bitmap?) {
        webView?.apply {
            if (!isAnimating) {
                loadingView(true)
            }
            when (url) {
                context?.getString(R.string.url_session).toString() -> {
                    runScript(JsScrip.GET_USERNAME) {
                        userName = it?.remove("\"").toString()
                    }
                    logoutCount = 0
                    loginCount++
                }
                context?.getString(R.string.url_github).toString() -> {
                    logoutCount = 0
                    loginCount++
                    if (loginCount == 3) {
                        authentication(true)
                    }
                }
                context.getString(R.string.url_logout) -> {
                    loginCount = 0
                    logoutCount++
                }
                else -> {
                    logoutCount = 0
                    loginCount = 0
                }
            }
        }
    }

    private fun authentication(isLogin: Boolean) {
        if (isLogin) {
            viewModel.updateUser(userName, isLogin)
        } else {
            context?.getString(R.string.url_login)?.let {
                baseUrl = it
                loadUrlWithAnimation(it)
            }
            viewModel.updateUser("", false)
        }
        setProfileActions(isLogin)
    }

    @Suppress("ComplexMethod")
    private fun setProfileActions(isLogin: Boolean) = context?.let {
        quickActionProfile = QuickAction(it, QuickAction.VERTICAL)
        if (isLogin) {
            quickActionProfile.apply {
                setColorRes(R.color.colorPrimary)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                addActionItem(
                    ActionItem(5, getString(R.string.app_settings), R.drawable.ic_settings),
                    ActionItem(4, getString(R.string.user_settings), R.drawable.ic_user_settings),
                    ActionItem(3, getString(R.string.log_out), R.drawable.ic_logout),
                    ActionItem(1, getString(R.string.profile), R.drawable.ic_user)
                )
            }
        } else {
            quickActionProfile.apply {
                setColorRes(R.color.colorPrimary)
                setTextColorRes(R.color.white)
                setEnabledDivider(false)
                addActionItem(
                    ActionItem(5, getString(R.string.app_settings), R.drawable.ic_settings),
                    ActionItem(4, getString(R.string.user_settings), R.drawable.ic_user_settings),
                    ActionItem(2, getString(R.string.log_in), R.drawable.ic_login),
                    ActionItem(1, getString(R.string.profile), R.drawable.ic_user)
                )
            }
        }
        quickActionProfile.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> loadIfUserNameSet(getString(R.string.url_github) + viewModel.getUserName())
                2 -> loadUrlWithAnimation(getString(R.string.url_login))
                3 -> loadIfUserNameSet(getString(R.string.url_logout))
                4 -> loadIfUserNameSet(getString(R.string.url_settings))
                5 -> replaceFragment(SettingsFragment.newInstance(), true)
            }
        }
    }

    @Suppress("ComplexMethod", "LongMethod")
    override fun onPageFinished(url: String) {
        webView?.apply {
            when {
                url.contains(getString(R.string.str_gist)) or
                    url.contains(getString(R.string.url_issues)) or
                    url.contains(getString(R.string.url_pulls)) or
                    url.contains(getString(R.string.url_notifications)) or
                    url.contains(getString(R.string.url_new)) or
                    url.contains(getString(R.string.url_settings))
                -> {
                    settings?.textZoom = TEXT_SIZE_LARGE
                    logoutCount = 0
                    loginCount = 0
                }
                url.contains(getString(R.string.url_login)) or
                    url.contains(getString(R.string.url_search)) or
                    url.contains(getString(R.string.url_trending)) or
                    url.contains(getString(R.string.str_organization)) or
                    url.contains(getString(R.string.str_google_play)) or
                    url.contains(getString(R.string.str_new)) or
                    !url.contains(getString(R.string.str_github)) or
                    (url == getString(R.string.url_github))
                -> {
                    settings?.textZoom = TEXT_SIZE_SMALL
                    logoutCount = 0
                    loginCount = 0
                }
                url.contains(context.getString(R.string.url_blank)) -> {
                    logoutCount = 0
                    loginCount = 0
                }
                url == context.getString(R.string.url_logout) -> {
                    settings?.textZoom = TEXT_SIZE_SMALL
                    logoutCount++
                    loginCount = 0
                    if (logoutCount == 4) {
                        authentication(false)
                    }
                }
                url.contains(context.getString(R.string.url_session)) -> {
                    webView?.runScript(JsScrip.GET_USERNAME) {
                        userName = it?.remove("\"").toString()
                    }
                    settings?.textZoom = TEXT_SIZE_SMALL
                    logoutCount = 0
                    loginCount++
                }
                url.contains(getString(R.string.str_stargazers)) -> {
                    settings?.textZoom = TEXT_SIZE_MEDIUM
                    logoutCount = 0
                    loginCount = 0
                }
                url.contains(viewModel.getUserName().toString()) -> {
                    settings?.textZoom = if (url.contains(getString(R.string.str_gist))) {
                        TEXT_SIZE_LARGE
                    } else {
                        TEXT_SIZE_SMALL
                    }
                    logoutCount = 0
                    loginCount = 0
                }
                else -> {
                    if (url.contains(getString(R.string.url_github))) {
                        settings?.textZoom = TEXT_SIZE_SMALL
                    } else {
                        settings?.textZoom = TEXT_SIZE_LARGE
                    }
                    logoutCount = 0
                    loginCount = 0
                }
            }
            viewModel.loadSettings().darkMode?.let {
                runScript(JsScrip.getDarkMode(it)) {
                    loadingView(false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
        if (MainActivity.uri != null) {
            loadUrlWithAnimation(MainActivity.uri)
            MainActivity.uri = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        webView?.onActivityResult(requestCode, resultCode, intent)
    }

    @Suppress("TooGenericExceptionCaught")
    @WithPermissions([Manifest.permission.WRITE_EXTERNAL_STORAGE])
    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        try {
            if (!AdvancedWebView.handleDownload(context, url, suggestedFilename)) {
                snacky("Download unsuccessful, download manager has been disabled on device")
            }
        } catch (e: Exception) {
            Crashlytics.logException(e)
            Crashlytics.log(
                Log.ERROR,
                "Download Unseccessful",
                e.message
            )
        }
    }

    override fun onExternalPageRequest(url: String?) {
        // todo not implemented yet
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        loadUrlWithAnimation(webView?.context?.getString(R.string.url_blank))
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main
}