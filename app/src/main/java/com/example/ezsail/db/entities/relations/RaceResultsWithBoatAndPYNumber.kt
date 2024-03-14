package com.example.ezsail.db.entities.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.RaceResult
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class RaceResultsWithBoatAndPYNumber(
    @Embedded val raceResult: RaceResult,
    @Relation(
        entity = Boat::class,
        parentColumn = "sailNo",
        entityColumn = "sail_no"
    )
    val boatWithPYNumber: @RawValue BoatWithPYNumber
): Parcelable
