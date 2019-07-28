package mustafaozhan.github.com.androcat.base

import androidx.lifecycle.ViewModel
import mustafaozhan.github.com.androcat.application.Application
import mustafaozhan.github.com.androcat.dagger.component.ViewModelComponent
import mustafaozhan.github.com.androcat.tools.DataManager
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
abstract class BaseViewModel : ViewModel() {

    protected val viewModelComponent: ViewModelComponent by lazy {
        Application.instance.component.viewModelComponent()
    }

    @Inject
    lateinit var dataManager: DataManager

    init {
        @Suppress("LeakingThis")
        inject()
    }

    protected abstract fun inject()

    open fun getSettings() = dataManager.loadSettings()

    open fun updateSettings(
        darkMode: Boolean? = null,
        sliderShown: Boolean? = null
    ) = dataManager.updateSettings(darkMode, sliderShown)
}