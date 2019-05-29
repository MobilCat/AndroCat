package mustafaozhan.github.com.androcat.tools

enum class Notification(val value: String) {
    NOTIFICATIONS("Notification"),
    NEWS_FEED("News Feed");

    companion object {
        fun fromString(str: String) =
            values().associateBy(Notification::value)[str]
    }
}