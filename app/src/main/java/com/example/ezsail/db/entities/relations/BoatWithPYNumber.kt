package com.example.ezsail.db.entities.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.PYNumbers

@Entity
data class BoatWithPYNumber(
    @Embedded val boat: Boat,
    @Relation(
        parentColumn = "boatClass",
        entityColumn = "ClassName"
    )
    val number: PYNumbers
)