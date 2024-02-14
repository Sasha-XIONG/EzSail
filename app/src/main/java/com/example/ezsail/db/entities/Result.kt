package com.example.ezsail.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["compId", "sailNo", "raceNo"])
data class Result (
    val compId: Int,
    val sailNo: String,
    val raceNo: Int,
    val numberOfLaps: Int,
    val code: String?,
    val timeInMillis: Long?,
    val correctedTimeInMillis: Long?,
    val points: Int?
)