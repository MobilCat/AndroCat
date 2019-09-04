package mustafaozhan.github.com.androcat.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mustafaozhan.github.com.androcat.application.Application
import mustafaozhan.github.com.androcat.dagger.component.BroadcastReceiverComponent
import mustafaozhan.github.com.androcat.tools.DataManager
import javax.inject.Inject

abstract class BaseBroadcastReceiver : BroadcastReceiver() {
    protected val broadcastReceiverComponent: BroadcastReceiverComponent by lazy {
        Application.instance.component.broadcastReceiverComponent()
    }

    @Inject
    lateinit var dataManager: DataManager

    init {
        inject()
    }

    abstract fun inject()

    override fun onReceive(context: Context, intent: Intent) = Unit
}
