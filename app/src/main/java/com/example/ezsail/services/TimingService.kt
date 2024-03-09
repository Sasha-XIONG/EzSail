package com.example.ezsail.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.ezsail.Constants.ACTION_START_SERVICE
import com.example.ezsail.Constants.NOTIFICATION_CHANNEL_ID
import com.example.ezsail.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.ezsail.Constants.NOTIFICATION_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ezsail.Constants.TIMER_UPDATE_INTERVAL
import com.example.ezsail.TimingUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimingService: LifecycleService() {

    // Timestamp when we start the timer
    private var timeStarted = 0L
    var serviceKilled = false

    @Inject
    // Inject base notification builder, which holds configuration for current notification builder
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    companion object {
        val timeRaceInMillis = MutableLiveData<Long>()
        val isTimerEnabled = MutableLiveData<Boolean>()
    }

    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()
    }

    // Post initial values for live data
    private fun postInitialValues() {
        timeRaceInMillis.postValue(0L)
        isTimerEnabled.postValue(false)
    }

    private fun startTimer() {
        isTimerEnabled.postValue(true)
        timeStarted = System.currentTimeMillis()

        // Use a coroutine to improve performance
        CoroutineScope(Dispatchers.Main).launch {
            while (isTimerEnabled.value!!) {
                // Post time
                timeRaceInMillis.postValue(System.currentTimeMillis() - timeStarted)

                // Delay the coroutine, so timeRaceInMillis will not update all the time
                delay(TIMER_UPDATE_INTERVAL)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if(it.action == ACTION_START_SERVICE) {
                startForegroundService()
            } else {
                Log.d("service", "stop")
                killService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Build and update notification
    private fun startForegroundService() {
        startTimer()

        // Setup notification manager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the Android version is Oreo or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create notification channel
            createNotificationChannel(notificationManager)
        }
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        // Update time displayed on the notification
        timeRaceInMillis.observe(this, Observer {
            if (!serviceKilled){
                val notification = currentNotificationBuilder
                    .setContentText(TimingUtility.getFormattedStopWatchTime(it))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })
    }

    // Create channel for notification
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        // Initialise notification channel
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW) // IMPORTANCE_LOW makes sure the notification will not come with sound
        // Pass the channel to notification manager
        notificationManager.createNotificationChannel(channel)
    }

    // Kill service
    private fun killService() {
        serviceKilled = true
        postInitialValues()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}