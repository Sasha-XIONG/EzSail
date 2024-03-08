package com.example.ezsail.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
//import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
//import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.db.entities.relations.OverallResultsWithBoat
import com.example.ezsail.db.entities.relations.RaceResultsWithBoat

@Dao
interface SailingDao {
    // Insert + Update, return id that is inserted
    @Upsert // If the entry already existed, update
    suspend fun upsertBoat(boat: Boat)

    @Upsert
    suspend fun upsertSeries(series: Series)

    @Upsert
    suspend fun upsertRace(race: Race)

    @Upsert
    suspend fun upsertOverallResult(overallResult: OverallResult)

    @Upsert
    suspend fun upsertRaceResult(raceResult: RaceResult)

    @Query("SELECT * FROM series ORDER BY id DESC")
    fun getAllSeries(): LiveData<List<Series>>

    @Query("SELECT * FROM race WHERE seriesId = :id ORDER BY raceNo")
    suspend fun getAllRacesBySeriesId(id: Int): List<Race>?

    @Query("SELECT * FROM overall_results ORDER BY nett")
    fun getAllOverallResult(): LiveData<List<OverallResult>>

    @Query("SELECT * FROM series WHERE title LIKE :title")
    fun searchSeriesByTitle(title: String?): LiveData<List<Series>>

    @Delete
    suspend fun deleteSeries(series: Series)

//    @Query("DELETE FROM race WHERE seriesId = :id AND raceNo = :raceNo")
//    suspend fun deleteRaceBySeriesIdAndRaceNo(id: Int, raceNo: Int)
    @Delete
    suspend fun deleteRace(race: Race)

    @Query("SELECT * FROM series WHERE id = :id")
    suspend fun getSeriesById(id: Int?): Series?

//    @Upsert
//    suspend fun insertRaceResult(raceResult: RaceResult)
//
//    @Upsert
//    suspend fun insertOverallResult(overallResult: OverallResult)
//
//    @Query("SELECT * FROM competition")
//    fun getAllCompetitions(): LiveData<List<Competition>>
//
//    // The results need to be processed to be shown on overall page and other different race pages
//    @Query("SELECT * FROM result WHERE compId = :compId")
//    fun getResultByCompId(compId: Int): LiveData<List<Result>>
//
//    @Query("SELECT * FROM boat WHERE sailNo = :sailNo")
//    suspend fun getBoatBySailNo(sailNo: String): Boat
//
//    @Delete
//    suspend fun deleteBoat(boat: Boat)

    @Transaction
    @Query("SELECT * FROM overall_results WHERE series_id = :id")
    fun getAllOverallResultsBySeriesId(id: Int): LiveData<List<OverallResultsWithBoat>>

//    @Transaction
//    @Query("SELECT sail_number FROM overall_results WHERE series_id = :id")
//    fun getAllCurrentBoatsBySeriesId(id: Int): LiveData<List<String>>0
    @Transaction
    @Query("SELECT * FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo")
    fun getAllRaceResultsBySeriesIdAndRaceNo(id: Int, raceNo: Int): LiveData<List<RaceResultsWithBoat>>
//    @Upsert
//    suspend fun upsertBoatSeriesCrossRef(crossRef: BoatSeriesCrossRef)
//
//    @Query("SELECT * FROM series WHERE id = :id")
//    fun getBoatsOfSeries(id: Int): LiveData<List<SeriesWithBoats>>
}