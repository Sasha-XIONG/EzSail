package com.example.ezsail.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ezsail.R
import com.example.ezsail.db.SailingDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PastCompetitionActivity : AppCompatActivity() {

    @Inject // Tell dagger to get the Dao object from AppModule
    lateinit var sailingDao: SailingDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_info)
        Log.d("dao", "yes: ${sailingDao.hashCode()}")

//        // test database
//        val boats = listOf(
//            Boat("1234", "LASER", "Joe Goff", "", "bala", "handiclap"),
//            Boat("2213", "LASER", "Jack Chicken", "aa", "bala", "handiclap"),
//            Boat("23-111", "SOLAR", "Mike Harper", "", "bala", "handiclap"),
//            Boat("2222", "SOLAR", "Mike Harper", "", "bala", "handiclap")
//        )
//
//        lifecycleScope.launch {
//            boats.forEach { dao.insertBoat(it) }
//        }
    }
}