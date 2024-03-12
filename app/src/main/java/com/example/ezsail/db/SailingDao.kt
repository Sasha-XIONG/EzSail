package com.example.ezsail.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.PYNumbers
//import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
//import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.db.entities.relations.OverallResultsWithBoat
import com.example.ezsail.db.entities.relations.RaceResultsWithBoatAndPYNumber

//import com.example.ezsail.db.entities.relations.RaceResultsWithBoat

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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRaceResult(raceResult: RaceResult)

    @Update
    suspend fun updateRaceResult(raceResult: RaceResult)

    @Upsert
    suspend fun upsertPYNumber(number: PYNumbers)

    @Delete
    suspend fun deleteRaceResult(raceResult: RaceResult)

    @Query("SELECT * FROM series ORDER BY id DESC")
    fun getAllSeries(): LiveData<List<Series>>

    @Query("SELECT * FROM race WHERE seriesId = :id ORDER BY raceNo")
    suspend fun getAllRacesBySeriesId(id: Int): List<Race>?

    @Query("SELECT * FROM overall_results ORDER BY nett")
    fun getAllOverallResult(): LiveData<List<OverallResult>>

    @Query("SELECT * FROM series WHERE title LIKE :title")
    fun searchSeriesByTitle(title: String?): LiveData<List<Series>>

    @Query("SELECT ClassName FROM PYNumbers")
    fun getAllBoatClass(): LiveData<List<String>>

    @Query("SELECT sail_no FROM Boat")
    fun getAllSailNo(): LiveData<List<String>>

    @Query("SELECT Number FROM PYNumbers WHERE ClassName = :boatClass")
    suspend fun getNumberByClass(boatClass: String): Int

    @Query("SELECT DISTINCT club FROM Boat ")
    fun getAllClub(): LiveData<List<String>>

    @Query("SELECT DISTINCT fleet FROM Boat")
    fun getAllFleet(): LiveData<List<String>>

    @Query("SELECT helm FROM Boat UNION SELECT crew FROM Boat")
    fun getAllSailors(): LiveData<List<String>>

    @Delete
    suspend fun deleteSeries(series: Series)

    @Delete
    suspend fun deleteRace(race: Race)

    @Delete
    suspend fun deleteOverallResult(overallResult: OverallResult)

    @Query("SELECT * FROM series WHERE id = :id")
    suspend fun getSeriesById(id: Int?): Series?

    @Transaction
    @Query("SELECT * FROM overall_results WHERE series_id = :id")
    fun getAllOverallResultsBySeriesId(id: Int): LiveData<List<OverallResultsWithBoat>>

    @Transaction
    @Query("SELECT * FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo")
    fun getAllRaceResultsBySeriesIdAndRaceNo(id: Int, raceNo: Int): LiveData<List<RaceResultsWithBoatAndPYNumber>>

    // Functions for rescore
    @Transaction
    @Query("SELECT * FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo")
    suspend fun getRaceResultsListBySeriesIdAndRaceNo(id: Int, raceNo: Int): List<RaceResultsWithBoatAndPYNumber>

    @Query("SELECT COUNT(sailNo) FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo AND code IN (0, 3, 4)")
    suspend fun getEntriesBySeriesIdAndRaceNo(id: Int, raceNo: Int): Int

    @Query("SELECT MAX(laps) FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo")
    suspend fun getMostLapsBySeriesIdAndRaceNo(id: Int, raceNo: Int): Int

    @Query("SELECT * FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo AND correctedTime IS NOT NULL AND code = 0 ORDER BY correctedTime")
    suspend fun getRankedRaceResults(id: Int, raceNo: Int): List<RaceResult>?

    @Query("SELECT correctedTime FROM RaceResult WHERE seriesId = :id AND raceNo = :raceNo AND correctedTime IS NOT NULL GROUP BY correctedTime HAVING COUNT(*)>1")
    suspend fun getDuplicatedCorrectedTimeList(id: Int, raceNo: Int): List<Float>?

    @Query("UPDATE RaceResult " +
            "SET points= " +
            "(" +
            "SELECT ROUND(AVG(points), 1) " +
            "FROM RaceResult " +
            "WHERE seriesId = :id AND raceNo = :raceNo AND correctedTime = :ct AND code = 0" +
            ")" +
            "WHERE seriesId = :id AND raceNo = :raceNo AND correctedTime = :ct AND code = 0")
    suspend fun setAveragePoint(id: Int, raceNo: Int, ct: Float)

    @Query("SELECT DISTINCT(sailNo) FROM RaceResult WHERE seriesId = :id AND code = 2")
    suspend fun getAllOODBySeriesId(id: Int): List<String>

    @Query("UPDATE RaceResult " +
            "SET points = CASE " +
            "WHEN (" +
            "SELECT ROUND(AVG(points), 1) " +
            "FROM RaceResult " +
            "WHERE seriesId = :id AND sailNo = :sailNo AND code IN (0, 3, 4)" +
            ") IS NULL THEN points " +
            "ELSE (" +
            "SELECT ROUND(AVG(points), 1) " +
            "FROM RaceResult " +
            "WHERE seriesId = :id AND sailNo = :sailNo AND code IN (0, 3, 4)" +
            ")" +
            "END " +
            "WHERE seriesId = :id AND sailNo = :sailNo AND code = 2")
    suspend fun updatePointsForOOD(id: Int, sailNo: String)

    @Query("SELECT sail_number FROM OVERALL_RESULTS WHERE series_id = :id")
    suspend fun getAllSailorsBySeriesId(id: Int): List<String>

    @Query("UPDATE RaceResult " +
            "SET isExcluded = 0 " +
            "WHERE seriesId = :id")
    suspend fun clearDiscardStateForAll(id: Int)

    @Query("UPDATE RaceResult " +
            "SET isExcluded = 1 " +
            "WHERE seriesId = :id AND sailNo = :sailNo AND raceNo IN " +
            "(" +
            "SELECT raceNo FROM RaceResult WHERE seriesId = :id AND sailNo = :sailNo " +
            "ORDER BY points DESC " +
            "LIMIT :discardRaces)")
    suspend fun discardHighestPoints(id: Int, sailNo: String, discardRaces: Int)

    @Query("UPDATE overall_results " +
            "SET nett = " +
            "(SELECT SUM(points) " +
            "FROM RaceResult " +
            "WHERE seriesId = :id AND sailNo = :sailNo AND isExcluded = 0)" +
            "WHERE series_id = :id AND sail_number = :sailNo")
    suspend fun calculateNettOfSailor(id: Int, sailNo: String)
}