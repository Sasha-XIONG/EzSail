package com.example.ezsail.repository

import com.example.ezsail.db.SailingDao
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.Competition
import com.example.ezsail.db.entities.Result
import javax.inject.Inject

class MainRepository @Inject constructor(
    val sailingDao: SailingDao
) {
    suspend fun insertBoat(boat: Boat) = sailingDao.insertBoat(boat)

    suspend fun insertCompetition(competition: Competition) = sailingDao.insertCompetition(competition)

    suspend fun insertResult(result: Result) = sailingDao.insertResult(result)

    fun getAllCompetitions() = sailingDao.getAllCompetitions()

    suspend fun getResultByCompId(compId: Int) = sailingDao.getResultByCompId(compId)

    suspend fun getBoatBySailNo(sailNo: String) = sailingDao.getBoatBySailNo(sailNo)

    suspend fun deleteBoat(boat: Boat) = sailingDao.deleteBoat(boat)
}