package mustafaozhan.github.com.androcat.tools

enum class JsScrip(val value: String) {
    NORMAL_MODE("normalMode.js"),
    DARK_MODE("darkMode.js"),
    GET_USERNAME("getUsername.js");

    companion object {
        fun getTheme(darkMode: Boolean) =
            if (darkMode) DARK_MODE else NORMAL_MODE
    }
}