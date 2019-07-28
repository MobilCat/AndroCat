package mustafaozhan.github.com.androcat.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.androcat.annontation.PerViewModel
import mustafaozhan.github.com.androcat.dagger.module.ViewModelModule
import mustafaozhan.github.com.androcat.main.activity.MainActivityViewModel
import mustafaozhan.github.com.androcat.main.fragment.MainFragmentViewModel
import mustafaozhan.github.com.androcat.settings.SettingsFragmentViewModel
import mustafaozhan.github.com.androcat.slider.SliderActivityViewModel

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@PerViewModel
@Subcomponent(modules = [(ViewModelModule::class)])
interface ViewModelComponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
    fun inject(sliderActivityViewModel: SliderActivityViewModel)
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}