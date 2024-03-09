package com.example.ezsail.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezsail.Constants.ACTION_START_SERVICE
import com.example.ezsail.Constants.ACTION_STOP_SERVICE
import com.example.ezsail.TimingUtility
import com.example.ezsail.adapter.RaceResultItemAdapter
import com.example.ezsail.databinding.FragmentRaceResultBinding
import com.example.ezsail.services.TimingService
import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.ezsail.R
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.relations.RaceResultsWithBoat
import com.example.ezsail.listeners.RaceResultEventListener
import com.example.ezsail.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Fragment shows view pager
class RaceResultListFragment(race: Race):
    Fragment(R.layout.fragment_race_result), RaceResultEventListener {
    // Initialise view binding
    private var raceResultBinding: FragmentRaceResultBinding? = null
    private val binding get() = raceResultBinding!!

    private val viewModel: MainViewModel by activityViewModels()

    // Initialise adapter for recycler view
    private lateinit var raceResultListAdapter: RaceResultItemAdapter

    private var isTimerEnabled = false
    private var currentTimeInMillis = 0L

    var race = race

    // Setup notification permission
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                updateTimer()
            }
        }

    companion object {
        const val QueryKey = "query_key"
        // Used to create ResultFragment
        fun getInstance(key: String, race: Race): RaceResultListFragment {
            val fragment = RaceResultListFragment(race)
            Bundle().also {
                it.putString(QueryKey, key)
                fragment.arguments = it
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        raceResultBinding = FragmentRaceResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show race date
//        requireActivity().findViewById<TextView>(R.id.tv_raceDate).apply {
//            val calendar = Calendar.getInstance().apply {
//                timeInMillis = race.timestamp
//            }
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            visibility = View.VISIBLE
//            text = dateFormat.format(calendar.time)
//        }

        // Check state of race
        if (!race.is_ongoing) {
            binding.toggleBtn.visibility = View.GONE
            binding.timer.visibility = View.GONE
            binding.pintv.text = "Points"
        }

        setupRaceResultRecyclerView()

        binding.toggleBtn.setOnClickListener {
            // Check notification permission
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                updateTimer()
            }
        }

        // Update timer in real time
        subscribeToObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        raceResultBinding = null
    }

    // Send intent to service
    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TimingService::class.java).also {
            it.action = action
            // Deliver intent to service
            requireContext().startService(it)
        }

    private fun updateTimer() {
        // Check whether the timer is enabled at the time
        if (isTimerEnabled) {
            sendCommandToService(ACTION_STOP_SERVICE)
            binding.toggleBtn.visibility = View.GONE
            binding.timer.visibility = View.GONE

            // Update state of race
            race.is_ongoing = false
            viewModel.upsertRace(race)
        } else {
            sendCommandToService(ACTION_START_SERVICE)
        }
    }

    private fun updateToggleBtn(isTimerEnabled: Boolean) {
        this.isTimerEnabled = isTimerEnabled
        if (!isTimerEnabled) {
            binding.toggleBtn.text = "START!"
        } else {
            binding.toggleBtn.text = "FINISH!"
        }
    }

    private fun subscribeToObservers() {
        TimingService.isTimerEnabled.observe(viewLifecycleOwner, Observer {
            updateToggleBtn(it)
        })

        TimingService.timeRaceInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = TimingUtility.getFormattedStopWatchTime(currentTimeInMillis)
            binding.timer.text = formattedTime
        })
    }

    private fun setupRaceResultRecyclerView() {
        raceResultListAdapter = RaceResultItemAdapter(this, race)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = raceResultListAdapter
        }

        activity?.let {
            viewModel.getAllRaceResultsOfCurrentSeriesAndCurrentRace(race.raceNo).observe(viewLifecycleOwner) {raceResult ->
                raceResultListAdapter.differ.submitList(raceResult)
            }
        }

        // Sync race result page with overall result page every time race result page is activated
        activity?.let {
            viewModel.getAllOverallResultsOfCurrentSeries().observe(viewLifecycleOwner) {overallResult ->
                overallResult.forEach {
                    viewModel.insertRaceResult(
                        RaceResult(
                            it.boat.sailNo,
                            race.raceNo,
                            it.overallResult.seriesId
                        ),
                        race.is_ongoing
                    )
                }
            }
        }
    }

    override fun onAddLaps(raceResult: RaceResult) {
        raceResult.laps += 1
        viewModel.updateRaceResult(raceResult)
    }

    override fun onFinish(raceResult: RaceResult) {
        raceResult.elapsedTime = TimingUtility.getFormattedStopWatchTimeInFloat(currentTimeInMillis)
        viewModel.updateRaceResult(raceResult)
    }

    override fun onItemClick(raceResultsWithBoat: RaceResultsWithBoat) {
        // Init information card
        val card = Dialog(requireContext())
        card.requestWindowFeature(Window.FEATURE_NO_TITLE)
        card.setCancelable(true)
        card.setContentView(R.layout.information_card)

        card.findViewById<TextView>(R.id.boatClass_content).text = raceResultsWithBoat.boat.boatClass
        card.findViewById<TextView>(R.id.sailNo_content).text = raceResultsWithBoat.boat.sailNo
        card.findViewById<TextView>(R.id.helm_content).text = raceResultsWithBoat.boat.helm
        card.findViewById<TextView>(R.id.crew).text = raceResultsWithBoat.boat.crew
        card.findViewById<TextView>(R.id.club).text = raceResultsWithBoat.boat.club
        card.findViewById<TextView>(R.id.fleet).text = raceResultsWithBoat.boat.fleet
        card.findViewById<TextView>(R.id.rating).text = "1000"
        card.findViewById<TextView>(R.id.correctedTime).text = raceResultsWithBoat.raceResult.correctedTime.toString()
        card.findViewById<TextView>(R.id.points).text = raceResultsWithBoat.raceResult.points.toString()

        card.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            Toast.makeText(context, "Result Deleted", Toast.LENGTH_SHORT).show()
        }

        card.findViewById<Button>(R.id.editBtn).setOnClickListener {

        }

        card.show()
    }
}