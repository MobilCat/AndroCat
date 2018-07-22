package mustafaozhan.github.com.githubclient.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import mustafaozhan.github.com.githubclient.annontation.ApplicationContext
import mustafaozhan.github.com.githubclient.application.Application
import java.util.concurrent.Executors
import javax.inject.Singleton

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return application.applicationContext
    }

}