package mustafaozhan.github.com.androcat.main

import android.graphics.Bitmap
import android.webkit.WebView
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseWebViewClient
import mustafaozhan.github.com.androcat.extensions.remove
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.main.fragment.MainFragment
import mustafaozhan.github.com.androcat.tools.State

@Suppress("OverridingDeprecatedMember")
/**
 * Created by Mustafa Ozhan on 1/29/18 at 1:06 AM on Arch Linux wit Love <3.
 */
class MainWebViewClient(private var progressBarStateChangeListener: ProgressBarStateChangeListener)
    : BaseWebViewClient() {

    override fun inject() {
        webViewClientComponent.inject(this)
    }

    private var logoutCount = 0
    private var state: State = State.SUCCESS

    override fun onReceivedError(
        mWebView: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        mWebView?.loadUrl(mWebView.context.getString(R.string.url_blank))
        progressBarStateChangeListener.setProgressBarState(State.FAILED, dataManager.loadSettings().isInvert)
    }

    override fun onPageStarted(mWebView: WebView, url: String, favicon: Bitmap?) {
        progressBarStateChangeListener.animateProgressBar(true)

        if (url.contains(mWebView.context.getString(R.string.url_session))) {
            mWebView.runScript("getFields.js") { str ->
                if (str != "null")
                    dataManager.updateUser(str.remove("\""), true)
            }
            logoutCount = 0
        }
    }

    override fun onPageFinished(mWebView: WebView, url: String) {
        mWebView.apply {

            if (dataManager.loadSettings().isInvert) {
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
                        dataManager.updateUser(isLoggedIn = false)
                    }
                }
            }
        }
        progressBarStateChangeListener.animateProgressBar(false)
        progressBarStateChangeListener.setProgressBarState(state, dataManager.loadSettings().isInvert)
    }
}