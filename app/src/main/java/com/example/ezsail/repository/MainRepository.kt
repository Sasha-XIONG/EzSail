package com.example.ezsail.repository

import com.example.ezsail.db.SailingDao
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
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

    suspend fun upsertRaceResult(raceResult: RaceResult) = sailingDao.upsertRaceResult(raceResult)

//    suspend fun insertRaceResult(raceResult: RaceResult) = sailingDao.insertRaceResult(raceResult)
//
    fun getAllSeries() = sailingDao.getAllSeries()

    fun searchSeries(query: String?) = sailingDao.searchSeriesByTitle(query)

    fun getAllOverallResults() = sailingDao.getAllOverallResult()

    suspend fun deleteSeries(series: Series) = sailingDao.deleteSeries(series)

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
}