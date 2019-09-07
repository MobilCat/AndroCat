package mustafaozhan.github.com.androcat.tools

import android.content.Context
import mustafaozhan.github.com.androcat.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class AccessTokenUtil(private val context: Context, url: String) : Callback {
    init {
        handleAccessToken(url)
    }

    private fun handleAccessToken(url: String) {
        context.apply {

            val tokenCode = url.substring(url.lastIndexOf("?code=") + 1)
                .split("=".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()

            val cleanToken = tokenCode[1]
                .split("&".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()

            val code = cleanToken[0]

            val urlOauth = HttpUrl.parse(getString(R.string.url_github_access_token))
                ?.newBuilder()
                ?.addQueryParameter("client_id", getString(R.string.client_id))
                ?.addQueryParameter("client_secret", getString(R.string.client_secret))
                ?.addQueryParameter("code", code)
                ?.build()
                .toString()

            val request = Request.Builder()
                .header("Accept", "application/json")
                .url(urlOauth)
                .build()

            OkHttpClient().newCall(request).enqueue(this@AccessTokenUtil)
        }
    }

    override fun onFailure(call: Call, e: IOException) = Unit

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            val jSonData = response.body()!!.string()
            val jsonObject = JSONObject(jSonData)
            val authToken = jsonObject.getString("access_token")
            GeneralSharedPreferences().updateUser(token = authToken)
        }
    }
}
