package mustafaozhan.github.com.androcat.main.fragment

import mustafaozhan.github.com.androcat.base.BaseViewModel
import mustafaozhan.github.com.androcat.extensions.isValidUsername

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    fun getUserName(): String? {
        val username = dataManager.loadUser().username
        return if (username.isValidUsername()) {
            username
        } else {
            null
        }
    }

    fun isLoggedIn() = dataManager.loadUser().isLoggedIn

    fun updateUser(username: String? = null, isLoggedIn: Boolean? = null, token: String? = null) =
        dataManager.updateUser(username, isLoggedIn, token)

    fun loadSettings() = getSettings()

    fun updateSetting(
        darkMode: Boolean? = null
    ) = updateSettings(darkMode)
}
