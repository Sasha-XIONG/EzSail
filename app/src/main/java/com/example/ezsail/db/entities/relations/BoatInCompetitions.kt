package com.example.ezsail.db.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.Competition

data class BoatInCompetitions (
    @Embedded val boat: Boat,

    @Relation(
        parentColumn = "sailNo",
        entityColumn = "compId",
        associateBy = Junction(Result::class)
    )
    val competitions: List<Competition>
)