package mustafaozhan.github.com.androcat.tools

import com.google.gson.Gson
import mustafaozhan.github.com.androcat.base.BaseSharedPreferences
import mustafaozhan.github.com.androcat.model.Settings
import mustafaozhan.github.com.androcat.model.User
import mustafaozhan.github.com.androcat.notifications.Notification
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
        notificationList: ArrayList<Pair<Notification, Boolean>>? = null
    ) {
        val settings = loadSettings()
        darkMode?.let { settings.darkMode = it }
        notificationList?.let { settings.notificationList = it }
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
                val notificationList = ArrayList<Pair<Notification, Boolean>>()
                Notification.values().forEach {
                    notificationList.add(Pair(it, false))
                }
                Settings(darkMode = false, notificationList = notificationList)
            }
}