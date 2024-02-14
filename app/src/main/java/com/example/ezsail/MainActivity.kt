package com.example.ezsail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.ezsail.db.SailingDao
import com.example.ezsail.db.SailingDatabase
import com.example.ezsail.db.entities.Boat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject // Tell dagger to get the Dao object from AppModule
    lateinit var sailingDao: SailingDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d("dao", "yes")

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