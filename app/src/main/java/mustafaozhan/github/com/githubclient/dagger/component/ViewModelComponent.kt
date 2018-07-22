package mustafaozhan.github.com.githubclient.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.githubclient.annontation.PerViewModel
import mustafaozhan.github.com.githubclient.dagger.module.ViewModelModule
import mustafaozhan.github.com.githubclient.main.activity.MainActivityViewModel
import mustafaozhan.github.com.githubclient.main.fragment.MainFragmentViewModel

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@PerViewModel
@Subcomponent(modules = [(ViewModelModule::class)])
interface ViewModelComponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
//    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}