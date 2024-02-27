package com.example.ezsail.ui.fragments.newcompetition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezsail.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.ezsail.R
import com.example.ezsail.ResultItem
import com.example.ezsail.TimingUtility
import com.example.ezsail.adapter.RaceResultItemAdapter
import com.example.ezsail.databinding.FragmentRaceResultBinding
import com.example.ezsail.services.TimingService

// Fragment shows view pager
class RaceResultListFragment: Fragment() {
    // Initialise view binding
    private var _binding: FragmentRaceResultBinding? = null
    private val binding get() = _binding!!

    // Initialise linear layout manager for recyclerview
    private lateinit var linearLayoutManager: LinearLayoutManager

    // TEST!!
    val results = arrayListOf<ResultItem>(
        ResultItem("LASER", "1234", 5),
        ResultItem("LASER", "1111", 2)
    )

    // Initialise adapter for recycler view
    private lateinit var adapter: RaceResultItemAdapter

    private var currentTimeInMillis = 0L

    companion object {
        const val QueryKey = "query_key"
        // Used to create ResultFragment
        fun getInstance(key: String): RaceResultListFragment {
            val fragment = RaceResultListFragment()
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
        _binding = FragmentRaceResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireActivity())
        // Set the layout manager as linearlayoutmanager
        binding.recyclerView.layoutManager = linearLayoutManager

        // TEST!!
        adapter = RaceResultItemAdapter(results)
        binding.recyclerView.adapter = adapter

        binding.toggleBtn.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    // Send intent to service
    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TimingService::class.java).also {
            it.action = action
            // Deliver intent to service
            requireContext().startService(it)
        }

//    private fun subscribeToObservers() {
//        TimingService.timeRaceInMillis.observe(viewLifecycleOwner, Observer {
//            currentTimeInMillis = it
//            val formattedTime = TimingUtility.getFormattedStopWatchTime(currentTimeInMillis)
//            binding.timer.text = formattedTime
//        })
//    }
}