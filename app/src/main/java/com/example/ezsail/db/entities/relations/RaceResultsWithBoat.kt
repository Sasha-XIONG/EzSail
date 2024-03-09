package com.example.ezsail.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.RaceResult

data class RaceResultsWithBoat(
    @Embedded val raceResult: RaceResult,
    @Relation(
        parentColumn = "sailNo",
        entityColumn = "sail_no"
    )
    val boat: Boat
)
