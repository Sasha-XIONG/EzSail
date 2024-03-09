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
    @NonNull
    var participants: Int = 0

//    fun setTitle(title: String) {
//        this.title = title
//    }
//
//    fun getTitle(): String {
//        return this.title
//    }
//
//    fun setParticipants(number: Int) {
//        this.participants = number
//    }
//
//    fun getParticipants(): Int {
//        return this.participants
//    }
}


