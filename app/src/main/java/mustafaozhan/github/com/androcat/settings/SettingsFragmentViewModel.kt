package mustafaozhan.github.com.androcat.settings

import mustafaozhan.github.com.androcat.base.BaseViewModel
import mustafaozhan.github.com.androcat.model.Settings

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SettingsFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    lateinit var userName: String

    fun initUsername() {
        userName = dataManager.loadUserName()
    }

    fun saveNewUserName(userName: String) {
        dataManager.persistUserName(userName)
    }

    fun updateInvertSettings(invert: Boolean) =
        dataManager.apply {
            updateSettings(
                Settings(invert)
            )
        }

    fun getSettings() = dataManager.loadSettings()
}