package mustafaozhan.github.com.androcat.main.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.jakewharton.rxbinding2.widget.textChanges
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
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.extensions.hideKeyboard
import mustafaozhan.github.com.androcat.extensions.initExplorerActions
import mustafaozhan.github.com.androcat.extensions.initProductionActions
import mustafaozhan.github.com.androcat.extensions.initProfileActions
import mustafaozhan.github.com.androcat.extensions.initStackActions
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.extensions.setBGColor
import mustafaozhan.github.com.androcat.extensions.setVisibleWithAnimation
import mustafaozhan.github.com.androcat.extensions.showKeyboard
import mustafaozhan.github.com.androcat.settings.SettingsFragment
import mustafaozhan.github.com.androcat.tools.JsScrip

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Suppress("MagicNumber", "TooManyFunctions")
class MainFragment : BaseMainFragment() {
    companion object {
        private const val ARGS_OPEN_URL = "ARGS_OPEN_URL"
        var TAG: String = MainFragment::class.java.simpleName
        fun newInstance(url: String) = MainFragment().apply {
            arguments = Bundle().apply {
                putString(ARGS_OPEN_URL, url)
            }
        }

        fun newInstance() = MainFragment()
    }

    private var quickActionProfile: QuickAction? = null
    private lateinit var quickActionExplore: QuickAction
    private lateinit var quickActionStack: QuickAction
    private lateinit var quickActionProduction: QuickAction

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            getString(ARGS_OPEN_URL)?.let { loadUrlWithAnimation(it) }
            clear()
        }
        init()
        setDash()
        initWebView()
        setListeners()
        setActionListeners()

        viewModel.loginSubject
            .subscribe({ isLoggedIn ->
                if (isLoggedIn) {
                    viewModel.updateUser(userName, isLoggedIn)
                } else {
                    context?.getString(R.string.url_login)?.let {
                        baseUrl = it
                        loadUrlWithAnimation(it)
                    }
                    viewModel.updateUser("", isLoggedIn)
                }
                quickActionProfile = setProfileActions(isLoggedIn)
            }, { Crashlytics.logException(it) }
            ).addTo(compositeDisposable)
    }

    private fun init() {
        context?.apply {
            getString(R.string.androcat_svg_path).let { path ->
                fillableLoader.setSvgPath(path)
                fillableLoaderDarkMode.setSvgPath(path)
            }
            quickActionExplore = initExplorerActions()
            quickActionStack = initStackActions()
            quickActionProduction = initProductionActions()
        }

        eTxtSearch.background.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        newsFeedFab.bringToFront()

        if (viewModel.isLoggedIn() == true) {
            quickActionProfile = setProfileActions(true)
            baseUrl = getString(R.string.url_github)
        } else {
            quickActionProfile = setProfileActions(false)
            baseUrl = getString(R.string.url_login)
        }

        viewModel.loadSettings().darkMode?.let { darkMode(it) }

        loadUrlWithAnimation(baseUrl)
    }

    private fun setProfileActions(isLoggedIn: Boolean): QuickAction? {
        return context?.initProfileActions(isLoggedIn)?.apply {
            setOnActionItemClickListener { item ->
                when (item.actionId) {
                    1 -> loadIfUserNameSet(getString(R.string.url_github) + viewModel.getUserName())
                    2 -> loadUrlWithAnimation(getString(R.string.url_login))
                    3 -> {
                        updateVariables(logout = false)
                        loadUrlWithAnimation(getString(R.string.url_logout))
                    }
                    4 -> loadIfUserNameSet(getString(R.string.url_settings))
                    5 -> replaceFragment(SettingsFragment.newInstance(), true)
                }
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
                R.id.nv_profile -> quickActionProfile?.show(mBottomNavigationView.getIconAt(4))
            }
            true
        }

        eTxtSearch.textChanges()
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

    private fun darkMode(darkMode: Boolean, changeSettings: Boolean = false) {
        loader = if (darkMode) {
            context?.let { fillableLoaderLayout?.setBGColor(it, R.color.colorPrimaryDark) }
            fillableLoader?.visibility = View.GONE
            fillableLoaderDarkMode
        } else {
            context?.let { fillableLoaderLayout?.setBGColor(it, R.color.white) }
            fillableLoaderDarkMode?.visibility = View.GONE
            webView?.reload()
            fillableLoader
        }
        webView?.runScript(JsScrip.getTheme(darkMode))
        if (changeSettings) viewModel.updateSetting(darkMode = darkMode)
    }

    override fun loadingView(show: Boolean) {
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
    }

    override fun getLayoutResId(): Int = R.layout.fragment_main
}