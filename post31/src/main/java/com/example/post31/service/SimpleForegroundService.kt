package com.example.post31.service

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.post31.Post31Application.Companion.NOTIFICATION_CHANNEL
import com.example.post31.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SimpleForegroundService : Service() {

    private val binder = SimpleServiceBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.getSerializable(Extras.ACTION) as ActionValues

        when (action) {
            ActionValues.START -> {
                val notification = createNotification()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    startForeground(
                        155,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                    )
                } else startForeground(155, notification)

                binder.setStatus(SimpleServiceState.Status.STARTED)
            }

            ActionValues.STOP -> {
                binder.setStatus(SimpleServiceState.Status.STOPPED)
                stopSelf()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?) = binder

    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setContentTitle("Simple service")
            .setContentText("Service is running...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        return notificationBuilder.build()
    }

    class SimpleServiceBinder : Binder() {
        private val coroutineScope = CoroutineScope(Dispatchers.Default)

        private val _state = MutableStateFlow(SimpleServiceState())
        val state = _state.asStateFlow()

        fun setStatus(status: SimpleServiceState.Status) = coroutineScope.launch {
            _state.emit(state.value.copy(
                status = status
            ))
        }
    }

    data class SimpleServiceState(val status: Status = Status.STOPPED) {
        enum class Status {
            STARTED, STOPPED;
        }
    }

    enum class ActionValues {
        START, STOP;
    }

    object Extras {
        const val ACTION = "action"
    }

    companion object {

        fun startService(context: Context) = launchService(
            context = context,
            action = ActionValues.START
        )

        fun stopService(context: Context) = launchService(
            context = context,
            action = ActionValues.STOP
        )

        fun canScheduleExactAlarms(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true

            return context.getSystemService(AlarmManager::class.java)
                .canScheduleExactAlarms()
        }

        fun scheduleStart(context: Context, delayMillis: Long) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val pendingIntent = PendingIntent.getForegroundService(
                context,
                START_REQUEST_CODE,
                createServiceIntent(context, ActionValues.START),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + delayMillis,
                pendingIntent
            )
        }

        private fun launchService(context: Context, action: ActionValues) {
            context.startForegroundService(createServiceIntent(context, action))
        }

        private fun createServiceIntent(context: Context, action: ActionValues) =
            Intent(context, SimpleForegroundService::class.java).apply {
                putExtra(Extras.ACTION, action)
            }

        private const val START_REQUEST_CODE = 155
    }
}
