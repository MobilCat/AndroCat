package mustafaozhan.github.com.androcat.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.androcat.annontation.PerBroadcastReceiver
import mustafaozhan.github.com.androcat.dagger.module.BroadcastReceiverModule
import mustafaozhan.github.com.androcat.notification.NotificationReceiver

@PerBroadcastReceiver
@Subcomponent(modules = [(BroadcastReceiverModule::class)])
interface BroadcastReceiverComponent {
    fun inject(notificationReceiver: NotificationReceiver)
}
