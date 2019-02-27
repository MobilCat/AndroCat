package mustafaozhan.github.com.androcat.dagger.component

import android.content.Context
import dagger.Component
import mustafaozhan.github.com.androcat.annontation.ApplicationContext
import mustafaozhan.github.com.androcat.dagger.module.ApplicationModule
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {
    @ApplicationContext
    fun context(): Context

    fun viewModelComponent(): ViewModelComponent

    fun webViewComponent(): WebViewComponent
}