package mustafaozhan.github.com.androcat.dagger.component

import dagger.Subcomponent
import mustafaozhan.github.com.androcat.annontation.PerBroadcastReceiver
import mustafaozhan.github.com.androcat.dagger.module.BroadcastReceiverModule

@PerBroadcastReceiver
@Subcomponent(modules = [(BroadcastReceiverModule::class)])
interface BroadcastReceiverComponent