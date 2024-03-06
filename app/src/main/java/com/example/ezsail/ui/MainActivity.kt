package com.example.ezsail.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.ezsail.Constants.ACTION_SHOW_RECORD_FRAGMENT
import com.example.ezsail.R
import com.example.ezsail.databinding.ActivityNewCompetitionBinding
import com.example.ezsail.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Dagger manages viewmodel factories
    // TODO: correct view model needed
    private val viewModel: MainViewModel by viewModels()

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
                    R.id.allSeriesFragment, R.id.homeFragment, R.id.setTitleFragment ->
                    {binding.toolbar.visibility = View.GONE
                        binding.bottomToolbar.visibility = View.GONE}
                    R.id.addCompetitorFragment -> {binding.bottomToolbar.visibility = View.GONE}
                    else -> {binding.toolbar.visibility = View.VISIBLE
                    binding.bottomToolbar.visibility = View.VISIBLE}
                }
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

    override fun onDestroy() {
        super.onDestroy()
    }
}

