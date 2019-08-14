package mustafaozhan.github.com.androcat.main.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_main.bottom_navigation_view
import kotlinx.android.synthetic.main.fragment_main.fab_news_feed
import kotlinx.android.synthetic.main.fragment_main.layout_fillable_loader
import kotlinx.android.synthetic.main.fragment_main.layout_find_in_page
import kotlinx.android.synthetic.main.fragment_main.layout_swipe_refresh
import kotlinx.android.synthetic.main.fragment_main.web_view
import kotlinx.android.synthetic.main.layout_fillable_loader.fillable_loader
import kotlinx.android.synthetic.main.layout_fillable_loader.fillable_loader_dark
import kotlinx.android.synthetic.main.layout_find_in_page.et_search
import kotlinx.android.synthetic.main.layout_find_in_page.view_dismiss
import kotlinx.android.synthetic.main.layout_find_in_page.view_search_next
import kotlinx.android.synthetic.main.layout_find_in_page.view_search_previous
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
            loadUrl(urlStr = getString(ARGS_OPEN_URL))
            clear()
        }
        init()
        setDash()
        initWebView()
        setListeners()
        setActionListeners()

        viewModel
            .loginSubject
            .subscribe(::handleLoginAction, ::logException)
            .addTo(compositeDisposable)
    }

    private fun handleLoginAction(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            viewModel.updateUser(userName, isLoggedIn)
        } else {
            loadUrl(R.string.url_login)
            viewModel.updateUser("", isLoggedIn)
        }
        quickActionProfile = setProfileActions(isLoggedIn)
    }

    private fun init() {
        context?.apply {
            quickActionExplore = initExplorerActions()
            quickActionStack = initStackActions()
            quickActionProduction = initProductionActions()
        }

        et_search.background.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        fab_news_feed.bringToFront()

        quickActionProfile = setProfileActions(viewModel.isLoggedIn() == true)

        viewModel.getSettings().darkMode?.let { darkMode(it) }

        loadUrl(urlStr = baseUrl)
    }

    private fun setProfileActions(isLoggedIn: Boolean): QuickAction? {
        baseUrl = getString(if (isLoggedIn) R.string.url_github else R.string.url_login)
        return context?.initProfileActions(isLoggedIn)?.apply {
            setOnActionItemClickListener { item ->
                when (item.actionId) {
                    1 -> loadIfUserNameSet(R.string.url_user_profile, true)
                    2 -> loadUrl(R.string.url_login)
                    3 -> loadUrl(R.string.url_logout)
                    4 -> loadIfUserNameSet(R.string.url_settings)
                    5 -> replaceFragment(SettingsFragment.newInstance(), true)
                }
            }
        }
    }

    private fun setListeners() {
        fab_news_feed.setOnClickListener { loadUrl(urlStr = baseUrl) }
        web_view?.setListener(getBaseActivity(), this)

        layout_swipe_refresh.setOnRefreshListener {
            loadUrl(urlStr = web_view?.url)
            layout_swipe_refresh.isRefreshing = false
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nv_explorer -> quickActionExplore.show(bottom_navigation_view.getIconAt(0))
                R.id.nv_stacks -> quickActionStack.show(bottom_navigation_view.getIconAt(1))
                R.id.nv_production -> quickActionProduction.show(bottom_navigation_view.getIconAt(3))
                R.id.nv_profile -> quickActionProfile?.show(bottom_navigation_view.getIconAt(4))
            }
            true
        }

        et_search.textChanges()
            .subscribe { txt ->
                web_view?.findAllAsync(txt.toString())
            }.addTo(compositeDisposable)

        view_search_next.setOnClickListener {
            web_view?.findNext(true)
            it.hideKeyboard()
        }
        view_search_previous.setOnClickListener {
            web_view?.findNext(false)
            it.hideKeyboard()
        }
        view_dismiss.setOnClickListener {
            et_search.setText("")
            layout_find_in_page.setVisibleWithAnimation(false)
            layout_find_in_page.hideKeyboard()
        }
    }

    @Suppress("ComplexMethod")
    private fun setActionListeners() {
        quickActionExplore.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> web_view?.goBack()
                2 -> web_view?.goForward()
                3 -> viewModel.getSettings().darkMode?.let { darkMode(!it, true) }
                4 -> loadUrl(R.string.url_search)
                5 -> {
                    layout_find_in_page.setVisibleWithAnimation(true)
                    et_search.showKeyboard()
                }
                6 -> loadUrl(R.string.url_trending)
            }
        }
        quickActionStack.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> loadIfUserNameSet(R.string.url_notifications)
                2 -> loadIfUserNameSet(R.string.url_user_stars, true)
                3 -> loadIfUserNameSet(R.string.url_user_repositories, true)
                4 -> loadIfUserNameSet(R.string.url_user_gists, true)
            }
        }
        quickActionProduction.setOnActionItemClickListener { item ->
            when (item.actionId) {
                1 -> loadIfUserNameSet(R.string.url_issues)
                2 -> loadIfUserNameSet(R.string.url_pulls)
                3 -> loadIfUserNameSet(R.string.url_user_projects, true)
                4 -> loadIfUserNameSet(R.string.url_new)
                5 -> loadIfUserNameSet(R.string.url_gist)
            }
        }
    }

    private fun setDash() = bottom_navigation_view.apply {
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
    private fun initWebView() = web_view?.apply {
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
            context?.let { layout_fillable_loader?.setBGColor(it, R.color.colorPrimaryDark) }
            fillable_loader?.visibility = View.GONE
            fillable_loader_dark
        } else {
            context?.let { layout_fillable_loader?.setBGColor(it, R.color.white) }
            fillable_loader_dark?.visibility = View.GONE
            web_view?.reload()
            fillable_loader
        }
        web_view?.runScript(JsScrip.getTheme(darkMode))
        if (changeSettings) viewModel.updateSettings(darkMode = darkMode)
    }

    override fun loadingView(show: Boolean) {
        if (show and !isAnimating) {
            isAnimating = true
            layout_fillable_loader?.setVisibleWithAnimation(true)
            loader?.visibility = View.VISIBLE
            fillable_loader?.start()
            fillable_loader_dark?.start()
        } else {
            layout_fillable_loader?.setVisibleWithAnimation(false)
            fillable_loader?.visibility = View.GONE
            fillable_loader_dark?.visibility = View.GONE
            fillable_loader?.reset()
            fillable_loader_dark?.reset()
            isAnimating = false
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_main
}
