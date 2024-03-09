package com.example.ezsail.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult

data class OverallResultsWithBoat(
    @Embedded val overallResult: OverallResult,
    @Relation(
        parentColumn = "sail_number",
        entityColumn = "sail_no"
    )
    val boat: Boat
)
