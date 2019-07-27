package mustafaozhan.github.com.androcat.main.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.extensions.hideKeyboard
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.extensions.setVisibleWithAnimation
import mustafaozhan.github.com.androcat.extensions.showKeyboard
import mustafaozhan.github.com.androcat.settings.SettingsFragment
import mustafaozhan.github.com.androcat.tools.JsScrip

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Suppress("MagicNumber")
class MainFragment : BaseMainFragment() {

    companion object {
        private const val ARGS_OPEN_URL = "ARGS_OPEN_URL"
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

    private lateinit var quickActionProfile: QuickAction
    private lateinit var quickActionExplore: QuickAction
    private lateinit var quickActionStack: QuickAction
    private lateinit var quickActionProduction: QuickAction

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initActions()
        setDash()
        initWebView()
        setListeners()
        setActionListeners()

        arguments?.getString(ARGS_OPEN_URL)?.let { url ->
            loadUrlWithAnimation(url)
            arguments?.clear()
        }

        viewModel
            .loginSubject
            .subscribe({ isLoggedIn ->
                if (isLoggedIn) {
                    viewModel.updateUser(userName, isLoggedIn)
                } else {
                    context?.getString(R.string.url_login)?.let {
                        baseUrl = it
                        loadUrlWithAnimation(it)
                    }
                    viewModel.updateUser("", false)
                }
                setProfileActions(isLoggedIn)
            }, { error ->
                Crashlytics.logException(error)
            }).addTo(compositeDisposable)
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
    }

    private fun initActions() {
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

    private fun darkMode(darkMode: Boolean, changeSettings: Boolean = false) {
        setDarkMode(darkMode)
        webView?.runScript(JsScrip.getTheme(darkMode))
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

    @Suppress("ComplexMethod")
    private fun setProfileActions(isLogin: Boolean) = context?.let {
        quickActionProfile = QuickAction(it, QuickAction.VERTICAL).apply {
            setColorRes(R.color.colorPrimary)
            setTextColorRes(R.color.white)
            setEnabledDivider(false)
            addActionItem(
                ActionItem(5, getString(R.string.app_settings), R.drawable.ic_settings),
                ActionItem(4, getString(R.string.user_settings), R.drawable.ic_user_settings),
                if (isLogin) {
                    ActionItem(3, getString(R.string.log_out), R.drawable.ic_logout)
                } else {
                    ActionItem(2, getString(R.string.log_in), R.drawable.ic_login)
                },
                ActionItem(1, getString(R.string.profile), R.drawable.ic_user)
            )

            setOnActionItemClickListener { item ->
                when (item.actionId) {
                    1 -> loadIfUserNameSet(getString(R.string.url_github) + viewModel.getUserName())
                    2 -> loadUrlWithAnimation(getString(R.string.url_login))
                    3 -> {
                        logoutCount = 0
                        loadUrlWithAnimation(getString(R.string.url_logout))
                    }
                    4 -> loadIfUserNameSet(getString(R.string.url_settings))
                    5 -> replaceFragment(SettingsFragment.newInstance(), true)
                }
            }
        }
    }
}