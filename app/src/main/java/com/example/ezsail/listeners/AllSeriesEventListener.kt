package com.example.ezsail.listeners

import com.example.ezsail.db.entities.Series

interface AllSeriesEventListener {

    fun onDelete(series: Series)

    fun onPublish(series: Series)
}