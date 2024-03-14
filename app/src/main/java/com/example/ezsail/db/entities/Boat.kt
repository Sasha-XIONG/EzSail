package com.example.ezsail.db.entities

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(indices = [Index(value = ["sail_no"], unique = true)])
@Parcelize
data class Boat (
    @ColumnInfo(name = "sail_no")
    var sailNo: String,
    var boatClass: String,
    var helm: String? = null,
    var crew: String? = null,
    var club: String? = null,
    var fleet: String? = null
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
