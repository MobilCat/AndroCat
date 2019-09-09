package mustafaozhan.github.com.androcat.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import mustafaozhan.github.com.androcat.R
import mustafaozhan.github.com.androcat.main.activity.MainActivity
import mustafaozhan.github.com.androcat.notification.model.Notification
import mustafaozhan.github.com.androcat.notification.model.NotificationType

object NotificationUtil {
    fun senNotification(notification: Notification, context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)

        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        notificationIntent.data = Uri.parse(
            notification.subject?.url.toString()
                .replace("api.", "")
                .replace("/repos", "")
        )

        val intent = PendingIntent.getActivity(context, notification.id?.toInt() ?: -1,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        with(NotificationManagerCompat.from(context)) {
            notify(
                notification.id?.toInt() ?: -1,
                NotificationCompat
                    .Builder(context, NotificationCompat.CATEGORY_SOCIAL)
                    .setSmallIcon(
                        when (notification.subject?.type.toString()) {
                            NotificationType.ISSUE.value -> R.drawable.ic_issue
                            NotificationType.PULL_REQUEST.value -> R.drawable.ic_pull_request
                            else -> R.drawable.ic_androcat_dash
                        }
                    )
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
                    .setContentTitle("[${notification.subject?.type}] ${notification.repository?.fullName}")
                    .setContentText(notification.subject?.title.toString())
                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context) {

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
