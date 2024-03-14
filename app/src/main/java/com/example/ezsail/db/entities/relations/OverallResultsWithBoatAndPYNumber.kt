package com.example.ezsail.db.entities.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OverallResultsWithBoatAndPYNumber(
    @Embedded val overallResult: OverallResult,
    @Relation(
        entity = Boat::class,
        parentColumn = "sail_number",
        entityColumn = "sail_no"
    )
    val boatWithPYNumber: @RawValue BoatWithPYNumber
): Parcelable
