package com.example.ezsail.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    // Dagger knows how to inject mainRepository although we don't specify that in the module
    val mainRepository: MainRepository
): ViewModel() {
    var numberOfRaces = 0
    var currentPage = 0
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
            raceResult.code = "DNC"
            mainRepository.insertRaceResult(raceResult)
        }
    }

    fun updateRaceResult(raceResult: RaceResult) = viewModelScope.launch {
        mainRepository.updateRaceResult(raceResult)
    }

    fun deleteRaceResult(raceResult: RaceResult) = viewModelScope.launch {
        mainRepository.deleteRaceResult(raceResult)
    }

    fun getAllSeries() = mainRepository.getAllSeries()

    fun searchSeries(query: String?) = mainRepository.searchSeries(query)

//    suspend fun getAllRacesOfSeries(): List<Race>? {
//        val raceList = viewModelScope.async {
//            mainRepository.getAllRacesBySeriesId(currentSeries.id)
//        }
//        return raceList.await()
//    }
    suspend fun getAllRacesOfSeries(): List<Race>? =
        mainRepository.getAllRacesBySeriesId(currentSeries.id)

    fun getAllBoatClass() = mainRepository.getAllBoatClass()

    fun getAllSailNo() = mainRepository.getAllSailNo()

    suspend fun getNumberByClass(boatClass: String): Int = mainRepository.getNumberByClass(boatClass)

    fun deleteSeries(series: Series) = viewModelScope.launch {
        mainRepository.deleteSeries(series)
    }

//    fun deleteRace(raceNo: Int) = viewModelScope.launch {
//        mainRepository.deleteRace(currentSeries.id, raceNo)
//    }
    fun deleteRace(race: Race) = viewModelScope.launch {
        mainRepository.deleteRace(race)
    }

//    fun getSeries(id: Int?) = viewModelScope.launch {
//        currentSeries = mainRepository.getSeriesById(id)
//    }

    fun getAllOverallResults() = mainRepository.getAllOverallResults()
//
//    fun insertRace(race: Race) = viewModelScope.launch {
//        mainRepository.upsertRace(race)
//    }

//    fun insertRaceResult(raceResult: RaceResult) = viewModelScope.launch {
//        mainRepository.insertRaceResult(raceResult)
//    }

    //TEST!!
    fun getAllOverallResultsOfCurrentSeries() =
        mainRepository.getAllOverallResultsBySeriesId(currentSeries.id)

    fun getAllRaceResultsOfCurrentSeriesAndCurrentRace(raceNo: Int) =
        mainRepository.getAllRaceResultsBySeriesIdAndRaceNo(currentSeries.id, raceNo)

}