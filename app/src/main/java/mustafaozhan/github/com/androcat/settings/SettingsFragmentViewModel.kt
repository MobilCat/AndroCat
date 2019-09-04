package mustafaozhan.github.com.androcat.settings

import mustafaozhan.github.com.androcat.base.BaseViewModel
import mustafaozhan.github.com.androcat.extensions.isValidUsername

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
class SettingsFragmentViewModel : BaseViewModel() {

    override fun inject() {
        viewModelComponent.inject(this)
    }

    fun updateUserName(userName: String) {
        dataManager.updateUser(username = userName)
    }

    fun getUserName(): String? {
        val username = dataManager.loadUser().username
        return if (username.isValidUsername()) {
            username
        } else {
            null
        }
    }
}
