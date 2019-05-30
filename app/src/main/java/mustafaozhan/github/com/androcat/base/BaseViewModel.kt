package mustafaozhan.github.com.androcat.base

import androidx.lifecycle.ViewModel
import mustafaozhan.github.com.androcat.application.Application
import mustafaozhan.github.com.androcat.dagger.component.ViewModelComponent
import mustafaozhan.github.com.androcat.notifications.Notification
import mustafaozhan.github.com.androcat.tools.DataManager
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
abstract class BaseViewModel : ViewModel() {

    protected val viewModelComponent: ViewModelComponent by lazy { Application.instance.component.viewModelComponent() }

    @Inject
    lateinit var dataManager: DataManager

    init {
        inject()
    }

    protected abstract fun inject()

    protected fun getSettings() = dataManager.loadSettings()

    protected fun updateSettings(
        darkMode: Boolean? = null,
        isFirstTime: Boolean? = null,
        notificationList: ArrayList<Pair<Notification, Boolean>>? = null
    ) = dataManager.updateSettings(darkMode, isFirstTime, notificationList)
}