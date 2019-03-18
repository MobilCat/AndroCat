package mustafaozhan.github.com.androcat.webview

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.extensions.fadeIO
import mustafaozhan.github.com.androcat.extensions.remove
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.extensions.setState
import mustafaozhan.github.com.androcat.main.fragment.MainFragment
import mustafaozhan.github.com.androcat.tools.GeneralSharedPreferences
import mustafaozhan.github.com.androcat.tools.State

@Suppress("OverridingDeprecatedMember")
/**
 * Created by Mustafa Ozhan on 1/29/18 at 1:06 AM on Arch Linux wit Love <3.
 */
class AndroCatWebViewClient(private val mProgressBar: ArchedImageProgressBar) : WebViewClient() {

    private var logoutCount = 0
    private var state: State = State.SUCCESS

    override fun onReceivedError(
        mWebView: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        mWebView?.loadUrl(mWebView.context.getString(R.string.url_blank))
        mProgressBar.setState(State.FAILED)
    }

    override fun onPageStarted(mWebView: WebView, url: String, favicon: Bitmap?) {
        mProgressBar.fadeIO(true)
        mProgressBar.visibility = View.VISIBLE

        mWebView.apply {
            when {
                url.contains(context.getString(R.string.url_session)) -> {
                    runScript("getFields.js") { str ->
                        GeneralSharedPreferences().updateUser(str.remove("\""), true)
                    }
                    logoutCount = 0
                }
            }
        }
    }

    override fun onPageFinished(mWebView: WebView, url: String) {
        mWebView.apply {
            runScript("hideDash.js")

            if (GeneralSharedPreferences().loadSettings().isInvert) {
                runScript("getInvertedColors.js")
            } else {
                runScript("getNormalColors.js")
            }
            state = State.SUCCESS

            when {
                url.contains(context.getString(R.string.url_blank)) -> {
                    state = State.FAILED
                }
                url.contains(context.getString(R.string.url_logout)) -> {
                    logoutCount++
                    if (logoutCount == 2) {
                        MainFragment.url = context.getString(R.string.url_login)
                        loadUrl(context.getString(R.string.url_login))
                        GeneralSharedPreferences().updateUser(isLoggedIn = false)
                    }
                }
            }
        }
        mProgressBar.fadeIO(false)
        mProgressBar.setState(state)
    }
}