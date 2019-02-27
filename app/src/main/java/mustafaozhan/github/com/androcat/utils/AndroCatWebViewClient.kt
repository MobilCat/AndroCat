package mustafaozhan.github.com.androcat.utils

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.api.github.GitHubApiHelper
import mustafaozhan.github.com.androcat.extensions.fadeIO
import mustafaozhan.github.com.androcat.extensions.remove
import mustafaozhan.github.com.androcat.extensions.runScript
import mustafaozhan.github.com.androcat.extensions.setState
import mustafaozhan.github.com.androcat.tools.GeneralSharedPreferences
import mustafaozhan.github.com.androcat.tools.State
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

@Suppress("OverridingDeprecatedMember")
/**
 * Created by Mustafa Ozhan on 1/29/18 at 1:06 AM on Arch Linux wit Love <3.
 */
class AndroCatWebViewClient(private val mProgressBar: ArchedImageProgressBar) : WebViewClient() {
    companion object {
        const val TEXT_SIZE_SMALL = 100
        const val TEXT_SIZE_MEDIUM = 124
        const val TEXT_SIZE_LARGE = 150
    }

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
            if (url.contains(context.getString(mustafaozhan.github.com.androcat.R.string.url_session))) {
                runScript("getFields.js") { str ->
                    doAsync {

                        val token = GitHubApiHelper().gitHubApiServices.getUserAccessToken(
                            "Basic bXVzdGFmYW96aGFuOjE4Mzc4MzdyYXN5b25hbGlzdDgxMzg=",
                            context.getString(R.string.client_id)
                        )

                        uiThread {
                            GeneralSharedPreferences().updateUser(str.remove("\""), true, token)
                        }
                    }
                }
            }
            if (url.contains(context.getString(R.string.url_app_auth))) {
                GeneralSharedPreferences().updateUser(token = url.remove(context.getString(R.string.url_app_auth)))
            }
        }
    }

    override fun shouldOverrideUrlLoading(mWebView: WebView?, url: String?): Boolean {
        mWebView?.loadUrl(url)
        return true
    }

    override fun onPageFinished(mWebView: WebView, url: String) {
        mWebView.apply {
            runScript("hideDash.js")
            context?.apply {

                when {
                    url.contains(getString(R.string.url_login)) ||
                        url.contains(getString(R.string.url_logout)) ||
                        url.contains(getString(R.string.url_search)) ||
                        url.contains(getString(R.string.url_gist_login)) ||
                        url.contains(getString(R.string.url_market_place)) ||
                        url.contains(getString(R.string.url_trending)) ||
                        url.contains(getString(R.string.str_organization)) ||
                        url.contains(getString(R.string.str_google_play)) ||
                        !url.contains(getString(R.string.str_github)) ||
                        url == getString(R.string.url_github) -> {

                        settings?.textZoom = TEXT_SIZE_SMALL
                        state = State.SUCCESS
                    }
                    url.contains(getString(R.string.url_session)) -> {
                        settings?.textZoom = TEXT_SIZE_SMALL
                        state = State.SUCCESS
//                        webView.loadUrl(
//                            "https://github.com/login/oauth/authorize?client_id=" +
//                                context.getString(R.string.client_id)
//                        ) TODO
                    }
                    url.contains(getString(R.string.str_stargazers)) -> {
                        settings?.textZoom = TEXT_SIZE_MEDIUM
                        state = State.SUCCESS
                    }
                    url.contains(getString(R.string.url_blank)) -> {
                        state = State.FAILED
                    }
                    else -> {
                        state = State.SUCCESS
                        settings?.textZoom = TEXT_SIZE_LARGE
                    }
                }
            }
        }
        mProgressBar.fadeIO(false)
        mProgressBar.setState(state)
    }
}