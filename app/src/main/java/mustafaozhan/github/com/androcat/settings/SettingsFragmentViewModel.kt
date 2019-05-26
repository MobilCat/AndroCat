package mustafaozhan.github.com.androcat.settings

import mustafaozhan.github.com.androcat.base.BaseViewModel

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SettingsFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    fun getUserName() = dataManager.loadUser().username

    fun updateUserName(userName: String) {
        dataManager.updateUser(username = userName)
    }

    fun loadSettings() = getSettings()

    fun updateSetting(darkMode: Boolean? = null, isFirstTime: Boolean? = null) =
        updateSettings(darkMode, isFirstTime)
}