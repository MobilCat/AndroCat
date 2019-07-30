package mustafaozhan.github.com.androcat.main.fragment

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.github.jorgecastillo.FillableLoader
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.fragment_main.webView
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseMvvmFragment
import mustafaozhan.github.com.androcat.extensions.isValidUsername
import mustafaozhan.github.com.androcat.extensions.remove
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.settings.SettingsFragment
import mustafaozhan.github.com.androcat.tools.JsScrip
import mustafaozhan.github.com.androcat.tools.TextSize

@Suppress("TooManyFunctions")
abstract class BaseMainFragment : BaseMvvmFragment<MainFragmentViewModel>(), AdvancedWebView.Listener {

    companion object {
        const val LOGIN_COUNTER_LIMIT = 3
        const val LOGOUT_COUNTER_LIMIT = 4
    }

    private var loginCount = 0
    private var logoutCount = 0
    protected lateinit var baseUrl: String
    protected var isAnimating = false
    protected var userName = ""
    protected var loader: FillableLoader? = null

    override fun onPageStarted(url: String, favicon: Bitmap?) {
        if (!isAnimating) loadingView(true)

        when (url) {
            context?.getString(R.string.url_session) -> {
                webView?.runScript(JsScrip.GET_USERNAME) {
                    userName = it?.remove("\"").toString()
                }
                updateVariables(login = true, logout = false)
            }
            context?.getString(R.string.url_github) -> updateVariables(login = true, logout = false)
            context?.getString(R.string.url_logout) -> updateVariables(login = false, logout = true)
            else -> updateVariables(login = false, logout = false)
        }
    }

    @Suppress("ComplexMethod")
    override fun onPageFinished(url: String) {
        when {
            url == context?.getString(R.string.url_logout) ->
                updateVariables(login = false, logout = true, textSize = TextSize.SMALL)
            url.contains(context?.getString(R.string.url_session).toString()) -> {
                webView?.runScript(JsScrip.GET_USERNAME) {
                    userName = it?.remove("\"").toString()
                }
                updateVariables(login = true, logout = false, textSize = TextSize.SMALL)
            }
            url.contains(getString(R.string.str_gist)) or
                url.contains(getString(R.string.url_issues)) or
                url.contains(getString(R.string.url_pulls)) or
                url.contains(getString(R.string.url_notifications)) or
                url.contains(getString(R.string.url_new)) or
                url.contains(getString(R.string.url_settings)) ->
                updateVariables(login = false, logout = false, textSize = TextSize.LARGE)
            url.contains(getString(R.string.url_login)) or
                url.contains(getString(R.string.url_search)) or
                url.contains(getString(R.string.url_trending)) or
                url.contains(getString(R.string.str_organization)) or
                url.contains(getString(R.string.str_google_play)) or
                url.contains(getString(R.string.str_new)) or
                !url.contains(getString(R.string.str_github)) or
                (url == getString(R.string.url_github)) ->
                updateVariables(login = false, logout = false, textSize = TextSize.SMALL)
            url.contains(context?.getString(R.string.url_blank).toString()) ->
                updateVariables(login = false, logout = false)
            url.contains(getString(R.string.str_stargazers)) ->
                updateVariables(login = false, logout = false, textSize = TextSize.MEDIUM)
            url.contains(viewModel.getUserName().toString()) -> updateVariables(
                login = false,
                logout = false,
                textSize = if (url.contains(getString(R.string.str_gist))) TextSize.LARGE else TextSize.SMALL
            )
            else -> updateVariables(
                login = false,
                logout = false,
                textSize = if (url.contains(getString(R.string.url_github))) TextSize.SMALL else TextSize.LARGE
            )
        }

        viewModel.getSettings().darkMode?.let {
            webView?.runScript(JsScrip.getTheme(it)) {
                loadingView(false)
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

    override fun onExternalPageRequest(url: String?) {}

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        loadUrlWithAnimation(webView?.context?.getString(R.string.url_blank))
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

    protected fun loadUrlWithAnimation(urlToLoad: String?) = urlToLoad?.let { url ->
        loadingView(true)
        webView?.loadUrl(url)
    }

    @Suppress("ComplexMethod")
    protected fun updateVariables(
        login: Boolean? = null,
        logout: Boolean? = null,
        textSize: TextSize? = null
    ) {
        login?.let { if (it) loginCount++ else loginCount = 0 }
        logout?.let { if (it) logoutCount++ else logoutCount = 0 }
        textSize?.let { webView?.settings?.textZoom = it.value }
        when {
            loginCount == LOGIN_COUNTER_LIMIT -> viewModel.authentication(true)
            logoutCount == LOGOUT_COUNTER_LIMIT -> viewModel.authentication(false)
        }
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    abstract override fun getLayoutResId(): Int

    protected abstract fun loadingView(show: Boolean)
}