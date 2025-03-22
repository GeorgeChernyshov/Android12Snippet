package com.example.pre31.helper

import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pre31.Pre31Application
import com.example.pre31.R

class NotificationHelper(private val context: Context) {

    val notificationLayout = RemoteViews(
        context.packageName,
        R.layout.notification_small
    )

    val notificationLayoutExpanded = RemoteViews(
        context.packageName,
        R.layout.notification_large
    )

    fun showCustomNotification() {
        val builder = NotificationCompat
            .Builder(context, Pre31Application.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setCustomContentView(notificationLayout)
            .setCustomBigContentView(notificationLayoutExpanded)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId++, builder.build())
        }
    }

    companion object {
        var notificationId = 0
    }
}