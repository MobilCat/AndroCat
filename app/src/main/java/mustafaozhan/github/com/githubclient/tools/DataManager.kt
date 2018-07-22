package mustafaozhan.github.com.githubclient.tools

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Singleton
class DataManager @Inject
constructor(private val generalSharedPreferences: GeneralSharedPreferences) {


    fun loadUserName() = generalSharedPreferences.loadUserName()

    fun persistUserName(userName:String) {
        generalSharedPreferences.persistUserName(userName)
    }
}