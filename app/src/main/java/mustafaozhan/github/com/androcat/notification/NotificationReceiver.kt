package mustafaozhan.github.com.androcat.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mustafaozhan.github.com.androcat.R
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
        senNotification(
            "Notification",
            context,
            NOTIFICATION_REQUEST_CODE
        )
    }

    fun setNotifications(context: Context) {
        cancelNotifications(context)

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

    fun cancelNotifications(context: Context) {
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

    private fun senNotification(name: String, context: Context, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(
                1,
                NotificationCompat
                    .Builder(context, NotificationCompat.CATEGORY_SOCIAL)
                    .setSmallIcon(R.drawable.ic_androcat_dash)
                    .setContentTitle("Notification Title")
                    .setContentText("Much longer text that cannot fit one line...")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {

        val channel = NotificationChannel(
            NotificationCompat.CATEGORY_SOCIAL,
            context.getString(R.string.dark_mode),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.feedback)
        }

        val notificationManager: NotificationManager? =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }
}
