package com.example.ezsail.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.PYNumbers
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series

@Database(
    entities = [
        Boat::class,
        Race::class,
        Series::class,
        OverallResult::class,
        RaceResult::class,
        PYNumbers::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SailingDatabase: RoomDatabase() {

    abstract fun getSailingDao(): SailingDao
    // dagger will handle the singleton functionality of the room database
}