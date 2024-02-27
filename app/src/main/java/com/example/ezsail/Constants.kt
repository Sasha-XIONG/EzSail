package com.example.ezsail

object Constants {

    const val SAILING_DATABASE_NAME = "sailing_db"

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_RECORD_FRAGMENT = "ACTION_SHOW_RECORD_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "timer_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Timer"
    const val NOTIFICATION_ID = 1 // 0 will not work

    // Delay for updating the timer
    const val TIMER_UPDATE_INTERVAL = 50L
}