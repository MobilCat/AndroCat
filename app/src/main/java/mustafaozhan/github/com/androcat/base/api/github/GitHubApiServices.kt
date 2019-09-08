package mustafaozhan.github.com.androcat.base.api.github

import io.reactivex.Observable
import mustafaozhan.github.com.androcat.model.Notification
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiServices {
    @GET("notifications")
    fun getNotifications(@Query("all") all: Boolean): Observable<List<Notification>>
}
