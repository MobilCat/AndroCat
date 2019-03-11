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
            when {
                url.contains(context.getString(R.string.url_session)) ->
                    runScript("getFields.js") { str ->
                        GeneralSharedPreferences().updateUser(str.remove("\""), true)
                    }
            }
        }
    }

    override fun shouldOverrideUrlLoading(mWebView: WebView?, url: String?): Boolean {
        mWebView?.loadUrl(url)

        if (url?.contains("?code=") == true) {
            mWebView?.context?.apply {

                val tokenCode = url.substring(url.lastIndexOf("?code=") + 1)
                    .split("=".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()

                val cleanToken = tokenCode[1]
                    .split("&".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()

                val code = cleanToken[0]
                val client = OkHttpClient()
                val requestUrl = HttpUrl.parse(getString(R.string.url_github_access_token))!!.newBuilder()

                requestUrl.addQueryParameter("client_id", getString(R.string.client_id))
                requestUrl.addQueryParameter("client_secret", getString(R.string.client_secret))
                requestUrl.addQueryParameter("code", code)

                val urlOauth = requestUrl.build().toString()

                val request = Request.Builder()
                    .header("Accept", "application/json")
                    .url(urlOauth)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            val jSonData = response.body()!!.string()
                            val jsonObject = JSONObject(jSonData)
                            val authToken = jsonObject.getString("access_token")
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
                        loadUrl(
                            context.getString(R.string.url_github_authorize) +
                                "?client_id=" +
                                context.getString(R.string.client_id))
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