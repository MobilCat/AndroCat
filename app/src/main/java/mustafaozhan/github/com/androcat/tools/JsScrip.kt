package mustafaozhan.github.com.androcat.tools

enum class JsScrip(val value: String) {
    GET_NORMAL_COLORS("getNormalColors.js"),
    GET_INVERTED_COLORS("getInvertedColors.js"),
    GET_USERNAME("getUsername.js");

    companion object {
        fun getInversion(inversion: Boolean) =
            if (inversion)
                GET_INVERTED_COLORS
            else
                GET_NORMAL_COLORS
    }
}