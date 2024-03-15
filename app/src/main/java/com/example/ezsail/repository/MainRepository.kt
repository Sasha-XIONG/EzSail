package com.example.ezsail.repository

import androidx.room.Query
import com.example.ezsail.db.SailingDao
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.PYNumbers
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import javax.inject.Inject

class MainRepository @Inject constructor(
    val sailingDao: SailingDao
) {
    suspend fun upsertBoat(boat: Boat) = sailingDao.upsertBoat(boat)

    suspend fun upsertSeries(series: Series) = sailingDao.upsertSeries(series)

    suspend fun upsertRace(race: Race) = sailingDao.upsertRace(race)

    suspend fun upsertOverallResult(overallResult: OverallResult) = sailingDao.upsertOverallResult(overallResult)

    suspend fun insertRaceResult(raceResult: RaceResult) = sailingDao.insertRaceResult(raceResult)

    suspend fun updateRaceResult(raceResult: RaceResult) = sailingDao.updateRaceResult(raceResult)

    suspend fun upsertPYNumber(number: PYNumbers) = sailingDao.upsertPYNumber(number)

    suspend fun deleteOverallResult(overallResult: OverallResult) = sailingDao.deleteOverallResult(overallResult)

    fun getAllSeries() = sailingDao.getAllSeries()

    suspend fun getAllRacesBySeriesId(id: Int) = sailingDao.getAllRacesBySeriesId(id)

    fun searchSeries(query: String?) = sailingDao.searchSeriesByTitle(query)

    fun searchBoatBySailNoAtOverallPage(query: String?, id: Int) =
        sailingDao.searchBoatBySailNoAtOverallPage(query, id)

    fun searchBoatBySailNoAtRacePage(query: String?, id: Int, raceNo: Int) =
        sailingDao.searchBoatBySailNoAtRacePage(query, id, raceNo)

    fun getAllBoatClass() = sailingDao.getAllBoatClass()

    fun getAllSailNo() = sailingDao.getAllSailNo()

    suspend fun getNumberByClass(boatClass: String) = sailingDao.getNumberByClass(boatClass)

    fun getAllClub() = sailingDao.getAllClub()

    fun getAllFleet() = sailingDao.getAllFleet()

    fun getAllSailors() = sailingDao.getAllSailors()

    suspend fun deleteSeries(series: Series) = sailingDao.deleteSeries(series)

    suspend fun deleteRace(race: Race) = sailingDao.deleteRace(race)

    fun getAllOverallResultsBySeriesId(id: Int) = sailingDao.getAllOverallResultsBySeriesId(id)

    fun getAllRaceResultsBySeriesIdAndRaceNo(id: Int, raceNo: Int) =
        sailingDao.getAllRaceResultsBySeriesIdAndRaceNo(id, raceNo)

    // Function for rescore
    suspend fun getRaceResultsListBySeriesIdAndRaceNo(id: Int, raceNo: Int) =
        sailingDao.getRaceResultsListBySeriesIdAndRaceNo(id, raceNo)

    suspend fun getEntriesBySeriesId(id: Int) =
        sailingDao.getEntriesBySeriesId(id)

    suspend fun getEntriesBySeriesIdAndRaceNo(id: Int, raceNo: Int) =
        sailingDao.getEntriesBySeriesIdAndRaceNo(id, raceNo)

    suspend fun getMostLapsBySeriesIdAndRaceNo(id: Int, raceNo: Int) =
        sailingDao.getMostLapsBySeriesIdAndRaceNo(id, raceNo)

    suspend fun getRankedRaceResults(id: Int, raceNo: Int) = sailingDao.getRankedRaceResults(id, raceNo)

    suspend fun getDuplicatedCorrectedTimeList(id: Int, raceNo: Int) =
        sailingDao.getDuplicatedCorrectedTimeList(id, raceNo)

    suspend fun setAveragePoint(id: Int, raceNo: Int, ct: Float) =
        sailingDao.setAveragePoint(id, raceNo, ct)

    suspend fun getAllOODBySeriesId(id: Int) = sailingDao.getAllOODBySeriesId(id)

    suspend fun updatePointsForOOD(id: Int, sailNo: String) =
        sailingDao.updatePointsForOOD(id, sailNo)

    suspend fun getAllOnGoingRacesBySeriesId(id: Int) =
        sailingDao.getAllOnGoingRacesBySeriesId(id)

    suspend fun clearDiscardStateForAll(id: Int) =
        sailingDao.clearDiscardStateForAll(id)

    suspend fun getAllSailorsBySeriesId(id: Int) =
        sailingDao.getAllSailorsBySeriesId(id)

    suspend fun discardHighestPoints(id: Int, sailNo: String, discardRaces: Int) =
        sailingDao.discardHighestPoints(id, sailNo,discardRaces)

    suspend fun calculateNettOfSailor(id: Int, sailNo: String) =
        sailingDao.calculateNettOfSailor(id, sailNo)

    // Function for publish
    suspend fun getOverallResultsListBySeriesId(id: Int) =
        sailingDao.getOverallResultsListBySeriesId(id)

    suspend fun getRaceResultsListBySeriesIdOrderByNettAndRaceNo(id: Int) =
        sailingDao.getRaceResultsListBySeriesIdOrderByNettAndRaceNo(id)

    suspend fun getRaceResultsListBySeriesIdOrderByRaceNoAndPoints(id: Int) =
        sailingDao.getRaceResultsListBySeriesIdOrderByRaceNoAndPoints(id)

    suspend fun getAllCodeUsedBySeriesId(id: Int) =
        sailingDao.getAllCodeUsedBySeriesId(id)
}