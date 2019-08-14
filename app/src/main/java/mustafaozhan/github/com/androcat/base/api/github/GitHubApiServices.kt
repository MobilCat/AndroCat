package mustafaozhan.github.com.androcat.base.api.github

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiServices {
    @GET("user")
    fun getUser(@Query("access_token") token: String?): Observable<Nothing>
}
