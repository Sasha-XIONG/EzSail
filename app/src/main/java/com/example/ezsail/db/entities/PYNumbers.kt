package com.example.ezsail.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PYNumbers(
    @PrimaryKey
    @NonNull
    val ClassName: String,
    @NonNull
    var Number: Int
)
