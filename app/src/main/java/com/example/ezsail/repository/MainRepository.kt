package com.example.ezsail.repository

import com.example.ezsail.db.SailingDao
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.PYNumbers
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
//import com.example.ezsail.db.entities.RaceResult
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

    suspend fun deleteRaceResult(raceResult: RaceResult) = sailingDao.deleteRaceResult(raceResult)

    suspend fun deleteOverallResult(overallResult: OverallResult) = sailingDao.deleteOverallResult(overallResult)

//    suspend fun insertRaceResult(raceResult: RaceResult) = sailingDao.insertRaceResult(raceResult)
//
    fun getAllSeries() = sailingDao.getAllSeries()

    suspend fun getAllRacesBySeriesId(id: Int) = sailingDao.getAllRacesBySeriesId(id)

    fun searchSeries(query: String?) = sailingDao.searchSeriesByTitle(query)

    fun getAllOverallResults() = sailingDao.getAllOverallResult()

    fun getAllBoatClass() = sailingDao.getAllBoatClass()

    fun getAllSailNo() = sailingDao.getAllSailNo()

    suspend fun getNumberByClass(boatClass: String) = sailingDao.getNumberByClass(boatClass)

    fun getAllClub() = sailingDao.getAllClub()

    fun getAllFleet() = sailingDao.getAllFleet()

    fun getAllSailors() = sailingDao.getAllSailors()

    suspend fun deleteSeries(series: Series) = sailingDao.deleteSeries(series)

//    suspend fun deleteRace(id: Int, raceNo: Int) = sailingDao.deleteRaceBySeriesIdAndRaceNo(id, raceNo)
    suspend fun deleteRace(race: Race) = sailingDao.deleteRace(race)

    suspend fun getSeriesById(id: Int?) = sailingDao.getSeriesById(id)
//
//    suspend fun getResultByCompId(compId: Int) = sailingDao.getResultByCompId(compId)
//
//    suspend fun getBoatBySailNo(sailNo: String) = sailingDao.getBoatBySailNo(sailNo)
//
//    suspend fun deleteBoat(boat: Boat) = sailingDao.deleteBoat(boat)

    //TEST!!
    fun getAllOverallResultsBySeriesId(id: Int) = sailingDao.getAllOverallResultsBySeriesId(id)

    fun getAllRaceResultsBySeriesIdAndRaceNo(id: Int, raceNo: Int) =
        sailingDao.getAllRaceResultsBySeriesIdAndRaceNo(id, raceNo)

    // Function for rescore
    suspend fun getRaceResultsListBySeriesIdAndRaceNo(id: Int, raceNo: Int) =
        sailingDao.getRaceResultsListBySeriesIdAndRaceNo(id, raceNo)

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

    suspend fun clearDiscardStateForAll(id: Int) =
        sailingDao.clearDiscardStateForAll(id)

    suspend fun getAllSailorsBySeriesId(id: Int) =
        sailingDao.getAllSailorsBySeriesId(id)

    suspend fun discardHighestPoints(id: Int, sailNo: String, discardRaces: Int) =
        sailingDao.discardHighestPoints(id, sailNo,discardRaces)

    suspend fun calculateNettOfSailor(id: Int, sailNo: String) =
        sailingDao.calculateNettOfSailor(id, sailNo)
}