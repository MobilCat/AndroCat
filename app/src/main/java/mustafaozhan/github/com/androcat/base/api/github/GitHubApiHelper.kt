package mustafaozhan.github.com.androcat.base.api.github

import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.api.BaseApiHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubApiHelper @Inject
constructor() : BaseApiHelper() {

    companion object {
        const val TOKEN = ""
        const val TIME_OUT: Long = 500
    }

    val gitHubApiServices: GitHubApiServices by lazy { initGitHubApiServices() }

    private fun initGitHubApiServices(): GitHubApiServices {
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
        clientBuilder.addInterceptor {
            it.proceed(createInterceptorRequest(it))
        }
        val endpoint = getString(R.string.api_auth)
        val retrofit = initRxRetrofit(endpoint, clientBuilder.build())
        return retrofit.create(GitHubApiServices::class.java)
    }

    private fun createInterceptorRequest(chain: Interceptor.Chain): Request {
        val original = chain.request()
        val builder = original.newBuilder()
//                .header("", TOKEN)
        return builder.build()
    }
}
