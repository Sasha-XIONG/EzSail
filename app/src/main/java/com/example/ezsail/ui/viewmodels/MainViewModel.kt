package com.example.ezsail.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
//import com.example.ezsail.db.entities.RaceResult
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
    lateinit var currentRace: Race

    fun getAllSeries() = mainRepository.getAllSeries()

    fun searchSeries(query: String?) = mainRepository.searchSeries(query)

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

    fun upsertRaceResult(raceResult: RaceResult, raceState: Boolean) = viewModelScope.launch {
        if(raceState) {
            mainRepository.upsertRaceResult(raceResult)
        } else {
            raceResult.code = "DNC"
            mainRepository.upsertRaceResult(raceResult)
        }
    }

    fun deleteSeries(series: Series) = viewModelScope.launch {
        mainRepository.deleteSeries(series)
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
    fun getAllOverallResultsOfCurrentSeries() = mainRepository.getAllOverallResultsBySeriesId(currentSeries.id)

    fun getAllRaceResultsOfCurrentSeriesAndCurrentRace() =
        mainRepository.getAllRaceResultsBySeriesIdAndRaceNo(currentSeries.id, 1)

}