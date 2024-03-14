package com.example.ezsail.listeners

import com.example.ezsail.db.entities.relations.OverallResultsWithBoatAndPYNumber

interface OverallResultEventListener {
    fun onItemClick(overallResultsWithBoatAndPYNumber: OverallResultsWithBoatAndPYNumber)
}