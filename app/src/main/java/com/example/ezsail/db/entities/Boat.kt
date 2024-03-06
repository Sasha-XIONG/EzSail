package com.example.ezsail.db.entities

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Boat (
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "sail_no")
    val sailNo: String,
    @NonNull
    val boatClass: String,
    var helm: String? = null,
    var crew: String? = null,
    var club: String? = null,
    var fleet: String? = null
): Parcelable
