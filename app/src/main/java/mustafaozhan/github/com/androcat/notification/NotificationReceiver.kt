package mustafaozhan.github.com.androcat.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.base.BaseBroadcastReceiver
import mustafaozhan.github.com.androcat.model.Notification

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
                    {
                        it.forEach { notification ->
                            senNotification(notification, context)
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

    private fun senNotification(notification: Notification, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(
                1,
                NotificationCompat
                    .Builder(context, NotificationCompat.CATEGORY_SOCIAL)
                    .setSmallIcon(R.drawable.ic_androcat_dash)
                    .setContentTitle(notification.subject?.title.toString())
                    .setContentText(notification.repository?.name.toString())
                    .setContentInfo(notification.subject?.type.toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {

        val channel = NotificationChannel(
            NotificationCompat.CATEGORY_SOCIAL,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager: NotificationManager? =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.createNotificationChannel(channel)
    }
}
