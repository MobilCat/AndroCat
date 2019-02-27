package mustafaozhan.github.com.androcat.base.api.github

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GitHubApiServices {
    @GET
    fun getUserAccessToken(@Header("Authorization") asd: String, @Query("client_id") clientId: String): String
}