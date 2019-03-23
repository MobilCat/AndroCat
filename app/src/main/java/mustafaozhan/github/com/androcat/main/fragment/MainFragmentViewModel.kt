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

    fun getUsername() = dataManager.loadUser().username

    fun updateInvertSettings(invert: Boolean) =
        dataManager.apply {
            updateSettings(
                Settings(invert)
            )
        }

    fun isLoggedIn() = dataManager.loadUser().isLoggedIn

    fun getSettings() = dataManager.loadSettings()
}
