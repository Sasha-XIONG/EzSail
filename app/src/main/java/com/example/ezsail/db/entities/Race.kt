package com.example.ezsail.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDateTime

@Entity(
    primaryKeys = ["seriesId", "raceNo"],
    foreignKeys = [
        ForeignKey(
            entity = Series::class,
            parentColumns = ["id"],
            childColumns = ["seriesId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )])
data class Race(
    val seriesId: Int,
    val raceNo: Int,
    val timestamp: Long,
    // true: Ongoing, false: Complete
    var is_ongoing: Boolean
)
