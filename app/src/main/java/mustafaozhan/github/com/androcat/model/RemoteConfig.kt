package mustafaozhan.github.com.androcat.model

import com.google.gson.annotations.SerializedName
import mustafaozhan.github.com.androcat.BuildConfig

data class RemoteConfig(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("update_url") val updateUrl: String,
    @SerializedName("force_version") val forceVersion: Int = BuildConfig.VERSION_CODE,
    @SerializedName("latest_version") val latestVersion: Int = BuildConfig.VERSION_CODE
)