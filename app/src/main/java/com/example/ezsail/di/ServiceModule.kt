package com.example.ezsail.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import com.example.ezsail.Constants
import com.example.ezsail.Constants.ACTION_SHOW_RECORD_FRAGMENT
import com.example.ezsail.R
import com.example.ezsail.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class) // The dependencies only exist when the service is active
// Provide all dependencies for the timing service
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ) = PendingIntent.getActivity(
        app,
        0,
        app.packageManager.getLaunchIntentForPackage("com.example.ezsail"),
        PendingIntent.FLAG_IMMUTABLE
    )

    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false) // Make sure notification is always active
        .setOngoing(true)  // Make sure the notification can't be swiped away
        .setSmallIcon(R.drawable.baseline_kayaking_24)
        .setContentTitle("EzSail")
        .setContentText("00:00")
        .setContentIntent(pendingIntent) // Pending intent leads to main activity when click on it
}