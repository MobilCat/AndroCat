package mustafaozhan.github.com.androcat.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import mustafaozhan.github.com.androcat.base.BaseBroadcastReceiver

class NotificationReceiver : BaseBroadcastReceiver() {
    companion object {
        private const val NOTIFICATION_REQUEST_CODE = 1
        private const val NOTIFICATION_INTERVAL: Long = 2000
    }

    override fun inject() {
        broadcastReceiverComponent.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        compositeDisposable.add(
            dataManager.getNotifications()
                .subscribe(
                    { notifications ->
                        notifications
                            .filter { it.unread == false } // todo make it true after development
                            .forEach { notification ->
                                NotificationUtil.senNotification(notification, context)
                            }
                        compositeDisposable.dispose()
                    }, {
                    Log.d("Test Error", it.toString())
                }
                )
        )
    }

    fun setNotificationReceiver(context: Context) {
        cancelNotificationReceiver(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            NOTIFICATION_INTERVAL,
            pendingIntent
        )
    }

    fun cancelNotificationReceiver(context: Context) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(pendingIntent)
    }

}
