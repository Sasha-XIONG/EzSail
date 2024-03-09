package com.example.ezsail.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezsail.Constants
import com.example.ezsail.OverallItem
import com.example.ezsail.R
import com.example.ezsail.TimingUtility
import com.example.ezsail.adapter.OverallResultItemAdapter
import com.example.ezsail.adapter.RaceResultItemAdapter
import com.example.ezsail.adapter.SeriesListAdapter
import com.example.ezsail.adapter.testAdapter
import com.example.ezsail.databinding.FragmentOverallResultBinding
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.services.TimingService
import com.example.ezsail.ui.viewmodels.MainViewModel

class OverallResultListFragment: Fragment(R.layout.fragment_overall_result) {

    lateinit var overallResult: OverallResult

    // Initialise view binding
    private var overallResultsbinding: FragmentOverallResultBinding? = null
    private val binding get() = overallResultsbinding!!

    private val viewModel: MainViewModel by activityViewModels()

    // Initialise adapter for recycler view
    private lateinit var overallResultListAdapter: OverallResultItemAdapter

    companion object {
        const val QueryKey = "query_key"
        // Used to create ResultFragment
        fun getInstance(key: String): OverallResultListFragment {
            val fragment = OverallResultListFragment()
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
        overallResultsbinding = FragmentOverallResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide race time


        setupOverallResultRecyclerView()

        // Set onClickListener for add competitor button
        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_seriesFragment_to_addCompetitorFragment)
        }
    }

    private fun setupOverallResultRecyclerView() {
        overallResultListAdapter = OverallResultItemAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = overallResultListAdapter
        }

        activity?.let {
            viewModel.getAllOverallResultsOfCurrentSeries().observe(viewLifecycleOwner) {overallResult ->
                overallResultListAdapter.differ.submitList(overallResult)
            }
        }
    }

}