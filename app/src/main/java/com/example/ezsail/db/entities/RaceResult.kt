package com.example.ezsail.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

// BoatRaceCrossRef
@Entity(
    indices = [Index(value = ["raceNo", "seriesId"])],
    primaryKeys = ["sailNo", "raceNo", "seriesId"],
    foreignKeys = [
        ForeignKey(
            entity = Boat::class,
            parentColumns = ["sail_no"],
            childColumns = ["sailNo"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ), ForeignKey(
            entity = Race::class,
            parentColumns = ["raceNo", "seriesId"],
            childColumns = ["raceNo", "seriesId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RaceResult(
    val sailNo: String,
    val raceNo: Int,
    val seriesId: Int,
) {
    var laps: Int = 0
    var code: String? = null
    var elapsedTime: Float? = null
    var correctedTime: Float? = null
    var points: Int = 0
    // false: not excluded, true: excluded
    var isExcluded: Boolean = false
}