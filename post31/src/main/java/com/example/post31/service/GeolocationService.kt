package com.example.post31.service

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.post31.Post31Application.Companion.NOTIFICATION_CHANNEL
import com.example.post31.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class GeolocationService : Service() {

    private val binder = GeolocationServiceBinder()
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val locationRequestHA = LocationRequest.create()
        .apply {
            interval = 10000
            fastestInterval = 3000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            binder.setLocation(result.lastLocation)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.getSerializable(Extras.ACTION) as ActionValues

        when (action) {
            ActionValues.START -> {
                val notification = createNotification()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    startForeground(
                        154,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                    )
                } else startForeground(154, notification)

                binder.setStatus(GeolocationServiceState.Status.UPDATING)
                startLocationUpdates()
            }

            ActionValues.STOP -> {
                binder.setStatus(GeolocationServiceState.Status.STOPPED)
                stopLocationUpdates()

                stopSelf()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setContentTitle("Recording")
            .setContentText("App is recording audio...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        return notificationBuilder.build()
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequestHA,
                locationCallback,
                null
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    enum class ActionValues {
        START, STOP;
    }

    object Extras {
        const val ACTION = "action"
        const val DATA = "data"
        const val RESULT_CODE = "resultCode"
    }

    inner class GeolocationServiceBinder : Binder() {
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        private val _state = MutableStateFlow(GeolocationServiceState.DEFAULT)
        val state = _state.asStateFlow()

        fun setLocation(location: Location?) = coroutineScope.launch {
            location?.let {
                Log.i("location", "${it.latitude}, ${it.longitude}")

                _state.emit(state.value.copy(
                    locations = state.value.locations + LocationWrapper(it)
                ))
            }
        }

        fun setStatus(status: GeolocationServiceState.Status) = coroutineScope.launch {
            _state.emit(state.value.copy(
                status = status
            ))
        }
    }

    data class GeolocationServiceState(
        val locations: List<LocationWrapper>,
        val status: Status
    ) {
        enum class Status {
            UPDATING, STOPPED;
        }

        companion object {
            val DEFAULT = GeolocationServiceState(
                locations = emptyList(),
                status = Status.STOPPED
            )
        }
    }

    @JvmInline
    value class LocationWrapper(private val location: Location) {
        val locationString
            get() = "${location.latitude}, ${location.longitude}"

        val timeString
            get() = Date.from(Instant.ofEpochMilli(location.time)).toString()
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
            val intent = Intent(context, GeolocationService::class.java)
            intent.putExtra(
                Extras.ACTION,
                action
            )

            context.startForegroundService(intent)
        }
    }
}