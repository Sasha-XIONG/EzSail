package com.example.ezsail

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import com.example.ezsail.ui.MainActivity
import java.util.concurrent.TimeUnit

object TimingUtility {

    fun getFormattedStopWatchTime(ms: Long): String {
        var milliseconds = ms
        // Convert milliseconds to nearest seconds
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        // Convert remaining milliseconds to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return "${if(minutes < 10) "0" else ""}$minutes:"+
                "${if(seconds < 10) "0" else ""}$seconds"
    }

    fun getFormattedStopWatchTimeInFloat(ms: Long): Float {
        var milliseconds = ms
        // Convert milliseconds to nearest seconds
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        // Convert remaining milliseconds to seconds
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        val str = "${if(minutes < 10) "0" else ""}$minutes."+
                "${if(seconds < 10) "0" else ""}$seconds"

        Log.d("format", "${str.toFloat()}")

        return str.toFloat()
    }
}