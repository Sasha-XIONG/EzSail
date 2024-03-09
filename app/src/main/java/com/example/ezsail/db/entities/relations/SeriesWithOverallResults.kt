//package com.example.ezsail.db.entities.relations
//
//import androidx.room.Embedded
//import androidx.room.Relation
//import com.example.ezsail.db.entities.OverallResult
//import com.example.ezsail.db.entities.Series
//
//data class SeriesWithOverallResults(
//    @Embedded val series: Series,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "seriesId",
//    )
//    val overallResults: List<OverallResult>
//)