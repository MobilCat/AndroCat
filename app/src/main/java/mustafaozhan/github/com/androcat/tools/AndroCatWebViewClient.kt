package mustafaozhan.github.com.androcat.tools

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.extensions.fadeIO
import mustafaozhan.github.com.androcat.extensions.setState

@Suppress("OverridingDeprecatedMember")
/**
 * Created by Mustafa Ozhan on 1/29/18 at 1:06 AM on Arch Linux wit Love <3.
 */
class AndroCatWebViewClient(private val mProgressBar: ArchedImageProgressBar) : WebViewClient() {
    private var state: State = State.SUCCESS
    override fun onReceivedError(mWebView: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        mWebView?.loadUrl(mWebView.context.getString(R.string.url_blank))
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
            loadUrl("javascript:(function() {" +
                    " document.getElementsByClassName('position-relative js-header-wrapper ')[0].style.display='none';" +
                    " document.getElementsByClassName('footer container-lg px-3')[0].style.display='none';" +
                    " })()")
            context?.apply {
                url?.apply {
                    when {
                        contains(getString(R.string.url_login))
                                || contains(getString(R.string.url_logout))
                                || contains(getString(R.string.url_search))
                                || contains(getString(R.string.url_gist_login))
                                || contains(getString(R.string.url_market_place))
                                || contains(getString(R.string.url_trending))
                                || contains(getString(R.string.str_organization))
                                || contains(getString(R.string.str_google_play))
                                || !contains(getString(R.string.str_github))
                                || this == getString(R.string.url_github) -> {

                            settings?.textZoom = 100
                            state = State.SUCCESS
                        }
                        contains(getString(R.string.str_stargazers)) -> {
                            settings?.textZoom = 124
                            state = State.SUCCESS
                        }
                        contains(getString(R.string.url_blank)) -> {
                            state = State.FAILED
                        }
                        else -> {
                            state = State.SUCCESS
                            settings?.textZoom = 150
                        }
                    }
                }
            }
        }
        mProgressBar.fadeIO(false)
        mProgressBar.setState(state)
    }
}