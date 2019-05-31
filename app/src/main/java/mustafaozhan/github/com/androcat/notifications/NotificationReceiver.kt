package mustafaozhan.github.com.androcat.notifications

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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d("Broadcast Receiver", "Receiver Running")
    }

    fun setNotifications(context: Context) {
        cancelNotifications(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            NOTIFICATION_INTERVAL,
            pendingIntent
        )
    }

    fun cancelNotifications(context: Context) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}