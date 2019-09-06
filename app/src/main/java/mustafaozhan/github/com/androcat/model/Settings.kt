package mustafaozhan.github.com.androcat.model

import org.joda.time.Instant

data class Settings(
    var darkMode: Boolean? = false,
    var sliderShown: Boolean? = false,
    var isNotificationOn: Boolean? = true,
    var adFreeActivatedDate: Instant?
)
