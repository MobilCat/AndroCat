package mustafaozhan.github.com.androcat.dagger.module

import android.content.BroadcastReceiver
import dagger.Module
import dagger.Provides

@Module
class BroadcastReceiverModule(private val broadcastReceiver: BroadcastReceiver) {

    @Provides
    internal fun providesBroadcastReceiver(): BroadcastReceiver {
        return broadcastReceiver
    }
}