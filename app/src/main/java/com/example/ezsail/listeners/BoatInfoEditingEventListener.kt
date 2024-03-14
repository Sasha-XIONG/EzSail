package com.example.ezsail.listeners

import com.example.ezsail.db.entities.relations.BoatWithPYNumber

interface BoatInfoEditingEventListener {
    fun onSaveClick(boatWithPYNumber: BoatWithPYNumber? = null)
}