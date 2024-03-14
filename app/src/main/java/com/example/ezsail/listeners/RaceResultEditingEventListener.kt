package com.example.ezsail.listeners

import com.example.ezsail.db.entities.RaceResult

interface RaceResultEditingEventListener {
    fun onSaveClick(raceResult: RaceResult)
}