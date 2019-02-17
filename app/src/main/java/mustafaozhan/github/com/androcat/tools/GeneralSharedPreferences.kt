package mustafaozhan.github.com.androcat.tools

import com.google.gson.Gson
import mustafaozhan.github.com.androcat.base.BaseSharedPreferences
import mustafaozhan.github.com.androcat.model.User
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
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    fun persistUser(user: User) = setStringEntry(USER, Gson().toJson(user))

    fun loadUser() = Gson().fromJson(getStringEntry(USER), User::class.java)

    fun persistUserName(userName: String) = setStringEntry(USERNAME, userName)

    fun loadUserName() = getStringEntry(USERNAME, "Please Enter Your GitHup Username")
}