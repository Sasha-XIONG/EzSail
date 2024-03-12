package com.example.ezsail.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.parcelize.Parcelize

// BoatSeriesCrossRef
@Entity(
    tableName = "overall_results",
    primaryKeys = ["sail_number", "series_id"],
    foreignKeys = [
        ForeignKey(
            entity = Series::class,
            parentColumns = ["id"],
            childColumns = ["series_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ), ForeignKey(
            entity = Boat::class,
            parentColumns = ["sail_no"],
            childColumns = ["sail_number"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )])
@Parcelize
data class OverallResult(
    @ColumnInfo(name = "sail_number")
    val sailNo: String,
    @ColumnInfo(name = "series_id", index = true)
    val seriesId: Int
): Parcelable {
    var nett: Float = 0f
}
