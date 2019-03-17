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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

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

    override fun shouldOverrideUrlLoading(mWebView: WebView?, url: String?): Boolean {
        mWebView?.loadUrl(url)

        if (url?.contains("?code=") == true) {
            mWebView?.context?.apply {

                val tokenCode = url
                    .substring(url.lastIndexOf("?code=") + 1)
                    .split("=".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
                    .split("&".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0]

                val urlOauth = HttpUrl
                    .parse(getString(R.string.url_github_access_token))
                    ?.newBuilder()
                    ?.addQueryParameter("client_id", getString(R.string.client_id))
                    ?.addQueryParameter("client_secret", getString(R.string.client_secret))
                    ?.addQueryParameter("code", tokenCode)
                    ?.build()
                    ?.toString()
                    ?: ""

                OkHttpClient()
                    .newCall(Request
                        .Builder()
                        .header("Accept", "application/json")
                        .url(urlOauth)
                        .build()
                    ).enqueue(object : Callback {

                        override fun onFailure(call: Call, e: IOException) {}

                        override fun onResponse(call: Call, response: okhttp3.Response) {
                            if (response.isSuccessful) {
                                val authToken = JSONObject(
                                    response
                                        .body()
                                        ?.string()
                                ).getString("access_token")

                                GeneralSharedPreferences().updateUser(token = authToken)
                            }
                        }
                    })
            }
        }
        return true
    }

    override fun onPageFinished(mWebView: WebView, url: String) {
        mWebView.apply {

            state = State.SUCCESS
            runScript("hideDash.js")

            if (GeneralSharedPreferences().loadSettings().isInvert) {
                runScript("getInvertedColors.js")
            } else {
                runScript("getNormalColors.js")
            }

            when {
                url.contains(context.getString(R.string.url_session)) -> {
                    loadUrl(context.getString(R.string.url_github_authorize) +
                        "?client_id=" +
                        context.getString(R.string.client_id))
                }
                url.contains(context.getString(R.string.url_blank)) -> {
                    state = State.FAILED
                }
                url.contains(context.getString(R.string.url_logout)) -> {
                    logoutCount++
                    if (logoutCount == 2) {
                        MainFragment.url = context.getString(R.string.url_github_authorize) +
                            "?client_id=" +
                            context.getString(R.string.client_id)
                        loadUrl(context.getString(R.string.url_github_authorize) +
                            "?client_id=" +
                            context.getString(R.string.client_id))
                        GeneralSharedPreferences().updateUser(isLoggedIn = false)
                    }
                }
            }
        }
        mProgressBar.fadeIO(false)
        mProgressBar.setState(state)
    }
}