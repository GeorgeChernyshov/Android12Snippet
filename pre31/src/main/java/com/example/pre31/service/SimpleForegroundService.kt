package com.example.pre31.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.pre31.Pre31Application.Companion.NOTIFICATION_CHANNEL
import com.example.pre31.R
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

        private fun launchService(context: Context, action: ActionValues) {
            val intent = Intent(context, SimpleForegroundService::class.java)
            intent.putExtra(
                Extras.ACTION,
                action
            )

            context.startForegroundService(intent)
        }
    }
}