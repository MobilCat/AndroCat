package mustafaozhan.github.com.githubclient.dagger.component

import android.content.Context
import dagger.Component
import mustafaozhan.github.com.githubclient.annontation.ApplicationContext
import mustafaozhan.github.com.githubclient.dagger.module.ApplicationModule
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
}