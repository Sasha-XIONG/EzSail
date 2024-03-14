package com.example.ezsail.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezsail.R
import com.example.ezsail.adapter.OverallResultItemAdapter
import com.example.ezsail.databinding.FragmentOverallResultBinding
import com.example.ezsail.db.entities.relations.OverallResultsWithBoatAndPYNumber
import com.example.ezsail.listeners.OverallResultEventListener
import com.example.ezsail.ui.viewmodels.MainViewModel

class OverallResultListFragment:
    Fragment(R.layout.fragment_overall_result), OverallResultEventListener {

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

        setupOverallResultRecyclerView()

        // Set onClickListener for add competitor button
        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_seriesFragment_to_addCompetitorFragment)
        }
    }

    private fun setupOverallResultRecyclerView() {
        overallResultListAdapter = OverallResultItemAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = overallResultListAdapter
        }

        activity?.let {
            viewModel.getAllOverallResultsOfCurrentSeries().observe(viewLifecycleOwner) {
                overallResultListAdapter.differ.submitList(it)
            }
        }
    }

    override fun onItemClick(overallResultsWithBoatAndPYNumber: OverallResultsWithBoatAndPYNumber) {
        // Init information card
        val card = Dialog(requireContext())
        card.requestWindowFeature(Window.FEATURE_NO_TITLE)
        card.setCancelable(true)
        card.setContentView(R.layout.information_card)

        card.findViewById<TextView>(R.id.boatClass_content).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.boat.boatClass
        card.findViewById<TextView>(R.id.sailNo_content).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.boat.sailNo
        card.findViewById<TextView>(R.id.helm_content).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.boat.helm
        card.findViewById<TextView>(R.id.crew).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.boat.crew
        card.findViewById<TextView>(R.id.club).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.boat.club
        card.findViewById<TextView>(R.id.fleet).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.boat.fleet
        card.findViewById<TextView>(R.id.rating).text =
            overallResultsWithBoatAndPYNumber.boatWithPYNumber.number.Number.toString()
        card.findViewById<TextView>(R.id.tv_correctedTime).visibility = View.GONE
        card.findViewById<TextView>(R.id.correctedTime).visibility = View.GONE

        card.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
            viewModel.deleteOverallResult(overallResultsWithBoatAndPYNumber.overallResult)
            Toast.makeText(context, "Boat deleted from series", Toast.LENGTH_SHORT).show()
            card.hide()
        }

        card.findViewById<Button>(R.id.editBtn).setOnClickListener {
            val direction =
                SeriesFragmentDirections.actionSeriesFragmentToOverallPageEditingFragment(overallResultsWithBoatAndPYNumber)
            findNavController().navigate(direction)
            card.hide()
        }

        card.show()
    }

}