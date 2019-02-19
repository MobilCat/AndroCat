package mustafaozhan.github.com.androcat.tools

import mustafaozhan.github.com.androcat.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Singleton
class DataManager @Inject
constructor(private val generalSharedPreferences: GeneralSharedPreferences) {

    fun loadUser() = generalSharedPreferences.loadUser()

    fun persistUser(user: User) = generalSharedPreferences.persistUser(user)

    fun loadUserName() = generalSharedPreferences.loadUserName()

    fun persistUserName(userName: String) = generalSharedPreferences.persistUserName(userName)
}