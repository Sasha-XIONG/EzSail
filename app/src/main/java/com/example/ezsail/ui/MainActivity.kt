package com.example.ezsail.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ezsail.Constants.ACTION_SHOW_RECORD_FRAGMENT
import com.example.ezsail.R
import com.example.ezsail.databinding.ActivityNewCompetitionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewCompetitionBinding
    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCompetitionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise fragment host
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        // Check whether it's launched by clicking on notification
        navigateToRaceFragment(intent)

        setSupportActionBar(binding.toolbar)

        // NavController is used to manage navigation between fragments
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.setTitleFragment -> {binding.toolbar.visibility = View.GONE
                        binding.bottomToolbar.visibility = View.GONE}
                    R.id.addCompetitorFragment -> {binding.bottomToolbar.visibility = View.GONE}
                    else -> {binding.toolbar.visibility = View.VISIBLE
                    binding.bottomToolbar.visibility = View.VISIBLE}
                }
            }
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Check whether it's launched by clicking on notification
        navigateToRaceFragment(intent)
    }

    // Check if the activity is launched by clicking notification
    private fun navigateToRaceFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_RECORD_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_recordResultFragment)
        }
    }
}

