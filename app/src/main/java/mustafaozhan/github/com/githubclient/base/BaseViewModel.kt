package mustafaozhan.github.com.githubclient.base

import android.arch.lifecycle.ViewModel
import mustafaozhan.github.com.githubclient.application.Application
import mustafaozhan.github.com.githubclient.dagger.component.ViewModelComponent
import mustafaozhan.github.com.githubclient.tools.DataManager
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


}