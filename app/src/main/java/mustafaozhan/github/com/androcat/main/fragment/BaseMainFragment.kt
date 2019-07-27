package mustafaozhan.github.com.androcat.main.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.crashlytics.android.Crashlytics
import com.github.jorgecastillo.FillableLoader
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.fragment_main.fillableLoaderLayout
import kotlinx.android.synthetic.main.fragment_main.webView
import kotlinx.android.synthetic.main.layout_fillable_loader.fillableLoader
import kotlinx.android.synthetic.main.layout_fillable_loader.fillableLoaderDarkMode
import me.piruin.quickaction.ActionItem
import me.piruin.quickaction.QuickAction
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.extensions.isValidUsername
import mustafaozhan.github.com.androcat.extensions.remove
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.extensions.setVisibleWithAnimation
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.settings.SettingsFragment
import mustafaozhan.github.com.androcat.tools.JsScrip

@Suppress("TooManyFunctions", "MagicNumber")
open class BaseMainFragment : BaseMvvmFragment<MainFragmentViewModel>(), AdvancedWebView.Listener {

    companion object {
        const val TEXT_SIZE_SMALL = 100
        const val TEXT_SIZE_MEDIUM = 124
        const val TEXT_SIZE_LARGE = 150
    }

    protected lateinit var quickActionProfile: QuickAction
    private var logoutCount = 0
    private var loginCount = 0
    private var userName = ""
    private var isAnimating = false
    protected lateinit var baseUrl: String

    protected var loader: FillableLoader? = null

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
                runScript(JsScrip.getTheme(it)) {
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

    protected fun loadIfUserNameSet(url: String) =
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

    @Suppress("ComplexMethod")
    protected fun setProfileActions(isLogin: Boolean) = context?.let {
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
                        loadIfUserNameSet(getString(R.string.url_logout))
                    }
                    4 -> loadIfUserNameSet(getString(R.string.url_settings))
                    5 -> replaceFragment(SettingsFragment.newInstance(), true)
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

    protected fun loadUrlWithAnimation(urlToLoad: String?) = urlToLoad?.let { url ->
        loadingView(true)
        webView?.loadUrl(url)
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main
}