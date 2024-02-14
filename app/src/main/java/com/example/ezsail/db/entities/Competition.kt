package com.example.ezsail.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Competition (
    var compName: String,
//    var compDate: Date? = null
) {
    @PrimaryKey(autoGenerate = true)
    var compId: Int? = null
}