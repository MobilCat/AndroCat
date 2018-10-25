package mustafaozhan.github.com.androcat.tools

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.extensions.fadeIO
import mustafaozhan.github.com.androcat.extensions.setState

@Suppress("OverridingDeprecatedMember")
/**
 * Created by Mustafa Ozhan on 1/29/18 at 1:06 AM on Arch Linux wit Love <3.
 */
class AndroCatMyWebViewClient(private val mProgressBar: ArchedImageProgressBar) : WebViewClient() {
    private var state: State = State.SUCCESS
    override fun onReceivedError(mWebView: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        mWebView?.loadUrl("about:blank")
        state = State.FAILED
        mProgressBar.setState(state)
    }

    override fun shouldOverrideUrlLoading(mWebView: WebView?, url: String?): Boolean {
        mWebView?.loadUrl(url)
        return true
    }

    override fun onPageStarted(mWebView: WebView?, url: String?, favicon: Bitmap?) {
        mProgressBar.fadeIO(true)
        mProgressBar.visibility = View.VISIBLE

    }

    override fun onPageFinished(mWebView: WebView?, url: String?) {
        mWebView?.apply {
            loadUrl("javascript:(function() { document.getElementsByClassName('position-relative js-header-wrapper ')[0].style.display='none'; \n document.getElementsByClassName('footer container-lg px-3')[0].style.display='none'; })()")
            url?.apply {
                if (contains("https://github.com/login")
                        || contains("https://github.com/logout")
                        || contains("https://github.com/search")
                        || contains("https://gist.github.com/login")
                        || contains("https://github.com/marketplace")
                        || contains("https://github.com/trending")
                        || contains("https://github.com/marketplace")
                        || contains("https://github.com/org")//for organizations
                        || this == "https://github.com/") {
                    settings?.textZoom = 100
                    state = State.SUCCESS
                } else if (contains("stargazers")) {
                    settings?.textZoom = 124
                    state = State.SUCCESS
                } else if (contains("about:blank")) {
                    state = State.FAILED
                } else {
                    state = State.SUCCESS
                    settings?.textZoom = 150
                }
            }
        }
        mProgressBar.fadeIO(false)
        mProgressBar.setState(state)
    }
}