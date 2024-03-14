package com.example.ezsail.db.entities

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Series(
    @NonNull
    var title: String,
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


