package com.example.ezsail.listeners

import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.relations.RaceResultsWithBoat

interface RaceResultEventListener {

    fun onAddLaps(raceResult: RaceResult)

    fun onFinish(raceResult: RaceResult)

    fun onItemClick(raceResultsWithBoat: RaceResultsWithBoat)
}