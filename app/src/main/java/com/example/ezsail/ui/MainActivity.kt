package com.example.ezsail.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ezsail.Constants.ACTION_SHOW_RECORD_FRAGMENT
import com.example.ezsail.R
import com.example.ezsail.databinding.ActivityMainBinding
import com.example.ezsail.ui.fragments.AllSeriesFragmentDirections
import com.example.ezsail.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Dagger manages viewmodel factories
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise fragment host
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        // Check whether it's launched by clicking on notification
        navigateToRaceFragment(intent)

        setSupportActionBar(binding.toolbar)

        // Set onClickListener for back button
        binding.toolbar.setNavigationOnClickListener {
            navHostFragment.findNavController().popBackStack()
        }

        // NavController is used to manage navigation between fragments
        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.allSeriesFragment,
                    R.id.homeFragment,
                    R.id.setTitleFragment,
                    R.id.webViewFragment ->
                    {binding.toolbar.visibility = View.GONE
                        binding.bottomToolbar.visibility = View.GONE}
                    R.id.addCompetitorFragment,
                    R.id.racePageEditingFragment,
                    R.id.overallPageEditingFragment ->
                    {binding.bottomToolbar.visibility = View.GONE}
                    else -> {binding.toolbar.visibility = View.VISIBLE
                    binding.bottomToolbar.visibility = View.VISIBLE}
                }
            }

        binding.rescoreBtn.setOnClickListener {
            binding.progressBar.visibility =View.VISIBLE
            viewModel.rescore()
            Handler(Looper.getMainLooper()).postDelayed({
                refreshFragment()
                binding.progressBar.visibility = View.GONE
            }, 2000)

            Toast.makeText(this, "Results Rescoring...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Check whether it's launched by clicking on notification
        navigateToRaceFragment(intent)
    }

    // Check if the activity is launched by clicking notification
    private fun navigateToRaceFragment(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_RECORD_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.global_action_seriesFragment)
        }
    }

    private fun refreshFragment() {
        val navController = navHostFragment.findNavController()
        val currentSeries = viewModel.currentSeries

        navController.popBackStack()
        val direction = AllSeriesFragmentDirections.actionAllSeriesFragmentToSeriesFragment(currentSeries)
        navController.navigate(direction)
    }
}

