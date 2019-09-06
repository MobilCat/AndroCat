package mustafaozhan.github.com.androcat.tools

import com.google.gson.Gson
import mustafaozhan.github.com.androcat.base.BaseSharedPreferences
import mustafaozhan.github.com.androcat.model.Settings
import mustafaozhan.github.com.androcat.model.User
import org.joda.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Singleton
class GeneralSharedPreferences @Inject
constructor() : BaseSharedPreferences() {

    companion object {
        const val GENERAL_SHARED_PREFS = "GENERAL_SHARED_PREFS"
        const val USERNAME = "username"
        const val USER = "user"
        const val SETTINGS = "settings"
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    fun updateUser(username: String? = null, isLoggedIn: Boolean? = null, token: String? = null) {
        val user = loadUser()
        username?.let { user.username = it }
        isLoggedIn?.let { user.isLoggedIn = it }
        token?.let { user.token = it }
        setStringEntry(USER, Gson().toJson(user))
    }

    fun updateSettings(
        darkMode: Boolean? = null,
        sliderShown: Boolean? = null,
        isNotificationOn: Boolean? = null,
        adFreeActivatedDate: Instant? = null
    ) {
        val settings = loadSettings()
        darkMode?.let { settings.darkMode = it }
        sliderShown?.let { settings.sliderShown = it }
        adFreeActivatedDate?.let { settings.adFreeActivatedDate = it }
        isNotificationOn?.let { settings.isNotificationOn = it }
        setStringEntry(SETTINGS, Gson().toJson(settings))
    }

    fun loadUser(): User {
        val user = Gson().fromJson(getStringEntry(USER), User::class.java)
            ?: User(null, false, null)
        if (user.username == null) {
            user.username = getStringEntry(USERNAME, "")
        }
        return user
    }

    fun loadSettings() =
        Gson().fromJson(getStringEntry(SETTINGS), Settings::class.java)
            ?: run {
                Settings(darkMode = false, sliderShown = false, isNotificationOn = true, adFreeActivatedDate = null)
            }
}
