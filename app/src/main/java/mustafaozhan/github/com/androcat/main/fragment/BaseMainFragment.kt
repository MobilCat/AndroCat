package mustafaozhan.github.com.androcat.main.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.github.jorgecastillo.FillableLoader
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.fragment_main.web_view
import kotlinx.android.synthetic.main.layout_fillable_loader.fillable_loader
import kotlinx.android.synthetic.main.layout_fillable_loader.fillable_loader_dark
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
        const val AUTHENTICATION_COUNTER = 3
    }

    private var loginCount = 0
    private var logoutCount = 0
    protected lateinit var baseUrl: String
    protected var isAnimating = false
    protected var userName = ""
    protected var loader: FillableLoader? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.getString(R.string.androcat_svg_path)?.let { path ->
            fillable_loader.setSvgPath(path)
            fillable_loader_dark.setSvgPath(path)
        }
    }

    override fun onPageStarted(url: String, favicon: Bitmap?) {
        if (!isAnimating) loadingView(true)

        when (url) {
            context?.getString(R.string.url_session) -> {
                web_view?.runScript(JsScrip.GET_USERNAME) {
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
        web_view?.apply {
            when {
                url == getString(R.string.url_logout) ->
                    updateVariables(login = false, logout = true, textSize = TextSize.SMALL)
                url.contains(getString(R.string.url_session)) or
                    url.contains(getString(R.string.url_login)) -> {
                    runScript(JsScrip.GET_USERNAME) {
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
                url.contains(getString(R.string.url_search)) or
                    url.contains(getString(R.string.url_trending)) or
                    url.contains(getString(R.string.str_organization)) or
                    url.contains(getString(R.string.str_google_play)) or
                    url.contains(getString(R.string.str_new)) or
                    !url.contains(getString(R.string.str_github)) or
                    (url == getString(R.string.url_github)) ->
                    updateVariables(login = false, logout = false, textSize = TextSize.SMALL)
                url.contains(getString(R.string.url_blank)) ->
                    updateVariables(login = false, logout = false)
                url.contains(getString(R.string.str_stargazers)) ->
                    updateVariables(login = false, logout = false, textSize = TextSize.MEDIUM)
                url.contains(viewModel.getUserName().toString()) -> updateVariables(
                    login = false,
                    logout = false,
                    textSize = if (url.contains(getString(R.string.str_gist))) TextSize.LARGE else TextSize.SMALL
                )
                else -> updateVariables(
                    textSize = if (url.contains(getString(R.string.url_github))) TextSize.SMALL else TextSize.LARGE
                )
            }

            viewModel.getSettings().darkMode?.let {
                runScript(JsScrip.getTheme(it)) {
                    loadingView(false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        web_view?.onResume()
        if (MainActivity.uri != null) {
            loadUrl(urlStr = MainActivity.uri)
            MainActivity.uri = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        web_view?.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        web_view?.onActivityResult(requestCode, resultCode, intent)
    }

    @Suppress("TooGenericExceptionCaught")
    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        try {
            if (!hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Permission ask
                ActivityCompat.requestPermissions(getBaseActivity() as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
            } else {
                if (!AdvancedWebView.handleDownload(context, url, suggestedFilename)) {
                    snacky("Download unsuccessful, download manager has been disabled on device")
                }
                loadUrl()
            }

        } catch (e: Exception) {
            logException(e)
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onExternalPageRequest(url: String?) {
        // Nothing Implemented yet
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        loadUrl(R.string.url_blank)
    }

    protected fun loadIfUserNameSet(urlId: Int, addUserName: Boolean = false) = viewModel.apply {
        when {
            isLoggedIn() == false -> loadUrl(R.string.url_login, true)
            getUserName().isValidUsername() ->
                loadUrl(urlStr = if (addUserName) getString(urlId, getUserName()) else getString(urlId))
            else -> snacky(getString(R.string.missUsername), getString(R.string.enter)) {
                replaceFragment(SettingsFragment.newInstance(), true)
            }
        }
    }

    protected fun loadUrl(urlId: Int? = null, updateBaseUrl: Boolean = false, urlStr: String? = null) {
        loadingView(true)
        urlStr?.let {
            web_view?.loadUrl(it)
        } ?: run {
            urlId?.let { id ->
                if (updateBaseUrl) baseUrl = getString(id)
                web_view?.loadUrl(getString(id))
            }
        }
    }

    @Suppress("ComplexMethod")
    protected fun updateVariables(
        login: Boolean? = null,
        logout: Boolean? = null,
        textSize: TextSize? = null
    ) {
        login?.let { if (it) loginCount++ else loginCount = 0 }
        logout?.let { if (it) logoutCount++ else logoutCount = 0 }
        textSize?.let { web_view?.settings?.textZoom = it.value }
        when {
            loginCount == AUTHENTICATION_COUNTER -> viewModel.authentication(true)
            logoutCount == AUTHENTICATION_COUNTER -> viewModel.authentication(false)
        }
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    abstract override fun getLayoutResId(): Int

    protected abstract fun loadingView(show: Boolean)
}