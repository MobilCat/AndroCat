package mustafaozhan.github.com.androcat.settings

import mustafaozhan.github.com.androcat.base.BaseViewModel

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

}