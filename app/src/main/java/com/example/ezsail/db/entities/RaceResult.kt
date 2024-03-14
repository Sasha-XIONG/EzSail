package com.example.ezsail.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.parcelize.Parcelize

// BoatRaceCrossRef
@Entity(
    indices = [
        Index(value = ["raceNo", "seriesId"]),
        Index(value = ["seriesId", "sailNo"]),
        Index(value = ["code"])],
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
        ), ForeignKey(
            entity = OverallResult::class,
            parentColumns = ["series_id", "sail_number"],
            childColumns = ["seriesId", "sailNo"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class RaceResult(
    val sailNo: String,
    val raceNo: Int,
    val seriesId: Int,
): Parcelable {
    var laps: Int = 0
    // 0: no code; 1: DNC; 2: OOD; 3: RET; 4: DNF
    var code: Int = 0
    var elapsedTime: Float? = null
    var correctedTime: Float? = null
    var points: Float = 0f
    // false: not excluded, true: excluded
    var isExcluded: Boolean = false
}