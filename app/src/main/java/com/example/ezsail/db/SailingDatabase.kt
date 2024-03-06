package com.example.ezsail.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.Competition
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Result
import com.example.ezsail.db.entities.Series

@Database(
    entities = [
        Boat::class,
        Race::class,
        Series::class,
        OverallResult::class,
        RaceResult::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SailingDatabase: RoomDatabase() {

    abstract fun getSailingDao(): SailingDao
    // dagger will handle the singleton functionality of the room database
}