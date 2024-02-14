package com.example.ezsail.db.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.Competition

data class CompetitionWithBoats (
    @Embedded val competition: Competition,

    @Relation(
        parentColumn = "compId",
        entityColumn = "sailNo",
        associateBy = Junction(Result::class)
    )
    val boats: List<Boat>
)