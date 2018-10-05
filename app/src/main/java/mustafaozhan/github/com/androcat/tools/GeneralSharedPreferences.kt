package mustafaozhan.github.com.androcat.tools

import mustafaozhan.github.com.androcat.base.BaseSharedPreferences
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
    }

    override val preferencesName: String
        get() = GENERAL_SHARED_PREFS

    fun persistUserName(userName: String) {
        setStringEntry(USERNAME, userName)
    }

    fun loadUserName() = getStringEntry(USERNAME, "Please Enter Your GitHup Username")
}