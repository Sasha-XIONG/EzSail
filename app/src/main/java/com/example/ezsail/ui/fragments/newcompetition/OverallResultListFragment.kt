package com.example.ezsail.ui.fragments.newcompetition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezsail.OverallItem
import com.example.ezsail.R
import com.example.ezsail.adapter.OverallResultItemAdapter
import com.example.ezsail.adapter.RaceResultItemAdapter
import com.example.ezsail.databinding.FragmentOverallResultBinding

class OverallResultListFragment: Fragment() {
    // Initialise view binding
    private var _binding: FragmentOverallResultBinding? = null
    private val binding get() = _binding!!

    // Initialise linear layout manager for recyclerview
    private lateinit var linearLayoutManager: LinearLayoutManager

    // TEST!!
    val results = arrayListOf<OverallItem>(
        OverallItem("LASER", "1234", 3, 1),
        OverallItem("LASER", "3321", 4, 2)
    )

    // Initialise adapter for recycler view
    private lateinit var adapter: OverallResultItemAdapter

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
        _binding = FragmentOverallResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireActivity())
        // Set the layout manager as linearlayoutmanager
        binding.recyclerView.layoutManager = linearLayoutManager

        // TEST!!
        adapter = OverallResultItemAdapter(results)
        binding.recyclerView.adapter = adapter

        // Set onClickListener for add competitor button
        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_recordResultFragment_to_addCompetitorFragment)
        }
    }
}