package mustafaozhan.github.com.androcat.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Response {

    @SerializedName("login")
    @Expose
    var login: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("node_id")
    @Expose
    var nodeId: String? = null
    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null
    @SerializedName("gravatar_id")
    @Expose
    var gravatarId: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("html_url")
    @Expose
    var htmlUrl: String? = null
    @SerializedName("followers_url")
    @Expose
    var followersUrl: String? = null
    @SerializedName("following_url")
    @Expose
    var followingUrl: String? = null
    @SerializedName("gists_url")
    @Expose
    var gistsUrl: String? = null
    @SerializedName("starred_url")
    @Expose
    var starredUrl: String? = null
    @SerializedName("subscriptions_url")
    @Expose
    var subscriptionsUrl: String? = null
    @SerializedName("organizations_url")
    @Expose
    var organizationsUrl: String? = null
    @SerializedName("repos_url")
    @Expose
    var reposUrl: String? = null
    @SerializedName("events_url")
    @Expose
    var eventsUrl: String? = null
    @SerializedName("received_events_url")
    @Expose
    var receivedEventsUrl: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("site_admin")
    @Expose
    var siteAdmin: Boolean? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("company")
    @Expose
    var company: String? = null
    @SerializedName("blog")
    @Expose
    var blog: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("hireable")
    @Expose
    var hireable: Boolean? = null
    @SerializedName("bio")
    @Expose
    var bio: String? = null
    @SerializedName("public_repos")
    @Expose
    var publicRepos: Int? = null
    @SerializedName("public_gists")
    @Expose
    var publicGists: Int? = null
    @SerializedName("followers")
    @Expose
    var followers: Int? = null
    @SerializedName("following")
    @Expose
    var following: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
    @SerializedName("private_gists")
    @Expose
    var privateGists: Int? = null
    @SerializedName("total_private_repos")
    @Expose
    var totalPrivateRepos: Int? = null
    @SerializedName("owned_private_repos")
    @Expose
    var ownedPrivateRepos: Int? = null
    @SerializedName("disk_usage")
    @Expose
    var diskUsage: Int? = null
    @SerializedName("collaborators")
    @Expose
    var collaborators: Int? = null
    @SerializedName("two_factor_authentication")
    @Expose
    var twoFactorAuthentication: Boolean? = null
    @SerializedName("plan")
    @Expose
    var plan: Plan? = null

}

class Plan {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("space")
    @Expose
    var space: Int? = null
    @SerializedName("private_repos")
    @Expose
    var privateRepos: Int? = null
    @SerializedName("collaborators")
    @Expose
    var collaborators: Int? = null

}