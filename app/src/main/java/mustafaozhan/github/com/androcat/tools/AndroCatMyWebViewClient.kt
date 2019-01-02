package mustafaozhan.github.com.androcat.tools

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.extensions.fadeIO
import mustafaozhan.github.com.androcat.extensions.setState
import mustafaozhan.github.com.androcat.tools.Links.Companion.GIST_LOGIN
import mustafaozhan.github.com.androcat.tools.Links.Companion.GITHUB
import mustafaozhan.github.com.androcat.tools.Links.Companion.LOGIN
import mustafaozhan.github.com.androcat.tools.Links.Companion.LOGOUT
import mustafaozhan.github.com.androcat.tools.Links.Companion.MARKET_PLACE
import mustafaozhan.github.com.androcat.tools.Links.Companion.SEARCH
import mustafaozhan.github.com.androcat.tools.Links.Companion.STR_BLANK
import mustafaozhan.github.com.androcat.tools.Links.Companion.STR_GITHUB
import mustafaozhan.github.com.androcat.tools.Links.Companion.STR_GOOGLE_PLAY
import mustafaozhan.github.com.androcat.tools.Links.Companion.STR_ORGANIZATION
import mustafaozhan.github.com.androcat.tools.Links.Companion.STR_STARGAZERS
import mustafaozhan.github.com.androcat.tools.Links.Companion.TRENDING

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
                when {
                    contains(LOGIN)
                            || contains(LOGOUT)
                            || contains(SEARCH)
                            || contains(GIST_LOGIN)
                            || contains(MARKET_PLACE)
                            || contains(TRENDING)
                            || contains(STR_ORGANIZATION)
                            || contains(STR_GOOGLE_PLAY)
                            || !contains(STR_GITHUB)
                            || this == GITHUB -> {

                        settings?.textZoom = 100
                        state = State.SUCCESS
                    }
                    contains(STR_STARGAZERS) -> {
                        settings?.textZoom = 124
                        state = State.SUCCESS
                    }
                    contains(STR_BLANK) -> {
                        state = State.FAILED
                    }
                    else -> {
                        state = State.SUCCESS
                        settings?.textZoom = 150
                    }
                }
            }
        }
        mProgressBar.fadeIO(false)
        mProgressBar.setState(state)
    }
}