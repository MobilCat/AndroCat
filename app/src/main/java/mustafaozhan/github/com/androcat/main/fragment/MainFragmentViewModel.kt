package mustafaozhan.github.com.androcat.main.fragment

import mustafaozhan.github.com.androcat.base.BaseViewModel
import mustafaozhan.github.com.androcat.model.Settings

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class MainFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    fun getUser() = dataManager.loadUser()

    fun getUsername() = dataManager.loadUserName()

    fun updateInvertSettings(invert: Boolean) =
        dataManager.apply {
            updateSettings(
                Settings(invert)
            )
        }

    fun getSettings() = dataManager.loadSettings()
}
