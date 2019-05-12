package mustafaozhan.github.com.androcat.main.fragment

import mustafaozhan.github.com.androcat.base.BaseViewModel

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    fun getUsername() = dataManager.loadUser().username

    fun isLoggedIn() = dataManager.loadUser().isLoggedIn

    fun updateUser(username: String? = null, isLoggedIn: Boolean? = null, token: String? = null) =
        dataManager.updateUser(username, isLoggedIn, token)

    fun loadSettings() = getSettings()

    fun updateSetting(isInvert: Boolean? = null, isFirstTime: Boolean? = null) =
        updateSettings(isInvert, isFirstTime)
}
