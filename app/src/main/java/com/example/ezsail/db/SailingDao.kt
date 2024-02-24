package com.example.ezsail.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.Competition
import com.example.ezsail.db.entities.Result

@Dao
interface SailingDao {
    // Insert + Update
    @Insert(onConflict = OnConflictStrategy.REPLACE) // If the entry already existed, update
    suspend fun insertBoat(boat: Boat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompetition(competition: Competition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Query("SELECT * FROM competition")
    fun getAllCompetitions(): LiveData<List<Competition>>

    // The results need to be processed to be shown on overall page and other different race pages
    @Query("SELECT * FROM result WHERE compId = :compId")
    fun getResultByCompId(compId: Int): LiveData<List<Result>>

    @Query("SELECT * FROM boat WHERE sailNo = :sailNo")
    suspend fun getBoatBySailNo(sailNo: String): Boat

    @Delete
    suspend fun deleteBoat(boat: Boat)
}