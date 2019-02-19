package mustafaozhan.github.com.androcat.model

data class User(
    val username: String,
    val isLoggedIn: Boolean? = null,
    val token: String? = null
)