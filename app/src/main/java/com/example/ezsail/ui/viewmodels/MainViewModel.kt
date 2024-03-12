package com.example.ezsail.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.PYNumbers
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.db.entities.relations.RaceResultsWithBoatAndPYNumber
import com.example.ezsail.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.math.log
import kotlin.math.round
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
    // Dagger knows how to inject mainRepository although we don't specify that in the module
    val mainRepository: MainRepository
): ViewModel() {
    lateinit var currentSeries: Series

    fun upsertBoat(boat: Boat) = viewModelScope.launch {
        mainRepository.upsertBoat(boat)
    }

    fun upsertSeries(series: Series) = viewModelScope.launch {
        mainRepository.upsertSeries(series)
    }

    fun upsertRace(race: Race) = viewModelScope.launch {
        mainRepository.upsertRace(race)
    }

    fun upsertOverallResult(overallResult: OverallResult) = viewModelScope.launch {
        mainRepository.upsertOverallResult(overallResult)
    }

    fun insertRaceResult(raceResult: RaceResult, isOngoing: Boolean) = viewModelScope.launch {
        if(isOngoing) {
            mainRepository.insertRaceResult(raceResult)
        } else {
            raceResult.code = 1
            mainRepository.insertRaceResult(raceResult)
        }
    }

    fun updateRaceResult(raceResult: RaceResult) = viewModelScope.launch {
        mainRepository.updateRaceResult(raceResult)
    }

    fun upsertPYNumber(number: PYNumbers) = viewModelScope.launch {
        mainRepository.upsertPYNumber(number)
    }

    fun deleteRaceResult(raceResult: RaceResult) = viewModelScope.launch {
        mainRepository.deleteRaceResult(raceResult)
    }

    fun deleteBoatFromCurrentSeries(overallResult: OverallResult) = viewModelScope.launch {
        mainRepository.deleteOverallResult(overallResult)
    }

    fun getAllSeries() = mainRepository.getAllSeries()

    fun searchSeries(query: String?) = mainRepository.searchSeries(query)

    suspend fun getAllRacesOfSeries(): List<Race>? =
        mainRepository.getAllRacesBySeriesId(currentSeries.id)

    fun getAllBoatClass() = mainRepository.getAllBoatClass()

    fun getAllSailNo() = mainRepository.getAllSailNo()

    fun getAllClub() = mainRepository.getAllClub()

    fun getAllFleet() = mainRepository.getAllFleet()

    fun getAllSailors() = mainRepository.getAllSailors()

    suspend fun getNumberByClass(boatClass: String): Int = mainRepository.getNumberByClass(boatClass)

    fun deleteSeries(series: Series) = viewModelScope.launch {
        mainRepository.deleteSeries(series)
    }

    fun deleteRace(race: Race) = viewModelScope.launch {
        mainRepository.deleteRace(race)
    }

    fun getAllOverallResults() = mainRepository.getAllOverallResults()

    fun getAllOverallResultsOfCurrentSeries() =
        mainRepository.getAllOverallResultsBySeriesId(currentSeries.id)

    fun getAllRaceResultsOfCurrentSeriesAndCurrentRace(raceNo: Int) =
        mainRepository.getAllRaceResultsBySeriesIdAndRaceNo(currentSeries.id, raceNo)

    // Functions for rescore
    suspend fun getRaceResultsListOfCurrentSeriesAndCurrentRace(raceNo: Int) =
        mainRepository.getRaceResultsListBySeriesIdAndRaceNo(currentSeries.id, raceNo)

    suspend fun getEntriesOfRace(raceNo: Int) =
        mainRepository.getEntriesBySeriesIdAndRaceNo(currentSeries.id, raceNo)

    suspend fun getMostLapsOfRace(raceNo: Int) =
        mainRepository.getMostLapsBySeriesIdAndRaceNo(currentSeries.id, raceNo)

    suspend fun getRankedRaceResults(raceNo: Int) =
        mainRepository.getRankedRaceResults(currentSeries.id, raceNo)

    //TEST!!
    suspend fun getDuplicatedCorrectedTimeList(raceNo: Int) =
        mainRepository.getDuplicatedCorrectedTimeList(currentSeries.id, raceNo)

    suspend fun setAveragePoint(raceNo: Int, ct: Float) =
        mainRepository.setAveragePoint(currentSeries.id, raceNo, ct)

    suspend fun getAllOODBySeriesId() =
        mainRepository.getAllOODBySeriesId(currentSeries.id)

    suspend fun updatePointsForOOD(sailNo: String) =
        mainRepository.updatePointsForOOD(currentSeries.id, sailNo)

    suspend fun getAllSailorsOfCurrentSeries() =
        mainRepository.getAllSailorsBySeriesId(currentSeries.id)

    suspend fun clearDiscardStateForAll() =
        mainRepository.clearDiscardStateForAll(currentSeries.id)

    suspend fun discardHighestPoints(sailNo: String, discardRaces:Int) =
        mainRepository.discardHighestPoints(currentSeries.id, sailNo, discardRaces)

    suspend fun calculateNettOfSailor(sailNo: String) =
        mainRepository.calculateNettOfSailor(currentSeries.id, sailNo)

    fun rescore() = viewModelScope.launch{
        val entriesOfSeries = currentSeries.participants
        val sailorList = getAllSailorsOfCurrentSeries()
        val numberOfRaces = getAllRacesOfSeries()?.size

        // numberOfDiscardRaces = ROUNDUP(numberOfRaces/2)-1
        // NOT NULL check
        val numberOfDiscardRaces = (numberOfRaces?.div(2f))?.roundToInt()?.minus(1)

        // Set isExcluded to 0 for all race results
        clearDiscardStateForAll()

        getAllRacesOfSeries()?.forEach {
            val raceNo = it.raceNo
            val mostLaps = getMostLapsOfRace(raceNo)

            if (!it.is_ongoing) {
                // Race results of the race
                val raceResults = getRaceResultsListOfCurrentSeriesAndCurrentRace(raceNo)
                // Entries don't include competitors with DNC/OOD
                val entriesOfRace = getEntriesOfRace(raceNo)

                raceResults.forEach {
                    // 1. Check code
                    when(it.raceResult.code) {
                        // No code applied
                        0 -> {
                            // 2. Calculate corrected time for competitors except OOD
                            // Corrected time = (Elapsed time x most laps x 1000) / (PN x actual laps)
                            val PN = it.boatWithPYNumber.number.Number
                            val laps = it.raceResult.laps
                            val correctedTime = it.raceResult.elapsedTime?.let {
                                // Round to 2 decimal places
                                round(it*mostLaps*1000/(PN*laps)*100)/100
                            }
                            it.raceResult.correctedTime = correctedTime
                            updateRaceResult(it.raceResult)
                        }
                        // DNC
                        1 -> {
                            it.raceResult.points = entriesOfSeries.toFloat()+1f
                            updateRaceResult(it.raceResult)
                        }
                        // OOD,RET, DNF
                        2, 3, 4 -> {
                            // Temporarily assign points for OOD
                            it.raceResult.points = entriesOfRace.toFloat()+1f
                            updateRaceResult(it.raceResult)
                        }
                    }
                }
                // Wait for list to be prepared
                delay(200)
                // 3. Rank corrected time to calculate points
                val rankedList = getRankedRaceResults(raceNo)
                rankedList?.forEach {
                    it.points = rankedList.indexOf(it) + 1f
                    updateRaceResult(it)
                }
                // 4. Calculate points for sailors with the same corrected time
                val duplicatedList = getDuplicatedCorrectedTimeList(raceNo)
                duplicatedList?.forEach {
                    setAveragePoint(raceNo, it)
                }
            }
        }
        // 5. Calculate average point for OOD
        val oodList = getAllOODBySeriesId()
        oodList.forEach {
            updatePointsForOOD(it)
        }
        // 6. Rank to get excluded race results
        sailorList.forEach {
            if (numberOfDiscardRaces != null) {
                discardHighestPoints(it, numberOfDiscardRaces)
            }
            // 7. Calculate nett for each sailor
            calculateNettOfSailor(it)
        }
    }

    // Function generate htm file
    fun publish()  = viewModelScope.launch {

    }
}