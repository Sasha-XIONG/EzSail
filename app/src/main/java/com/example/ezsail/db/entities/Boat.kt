package com.example.ezsail.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Boat (
    @PrimaryKey(autoGenerate = false)
    val sailNo: String,
    val boatClass: String,
    var helmName: String?,
    var crewName: String?,
    var club: String?,
    var fleet: String?
)
