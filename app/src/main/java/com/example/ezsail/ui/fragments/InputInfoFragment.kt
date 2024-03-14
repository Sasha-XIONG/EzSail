package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentEditBoatInfoBinding
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.PYNumbers
import com.example.ezsail.db.entities.relations.BoatWithPYNumber
import com.example.ezsail.listeners.BoatInfoEditingEventListener
import com.example.ezsail.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class InputInfoFragment(
    private val boatInfo: BoatWithPYNumber? = null
): Fragment(R.layout.fragment_edit_boat_info), BoatInfoEditingEventListener {
    // Set up view binding
    private var editBoatInfoBinding: FragmentEditBoatInfoBinding? = null
    private val binding get() = editBoatInfoBinding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editBoatInfoBinding = FragmentEditBoatInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init adapter for auto-complete text field
        subscribeToObservers()

        // In edit page, initialise view
        boatInfo?.apply {
            initView(boatInfo)
        }
    }

    private fun subscribeToObservers() {
        viewModel.getAllBoatClass().observe(viewLifecycleOwner) {
            setupAutoCompleteTextField(it, binding.boatClass)
        }

        viewModel.getAllSailNo().observe(viewLifecycleOwner) {
            setupAutoCompleteTextField(it, binding.sailNo)
        }

        viewModel.getAllClub().observe(viewLifecycleOwner) {
            setupAutoCompleteTextField(it, binding.tvClub)
        }

        viewModel.getAllFleet().observe(viewLifecycleOwner) {
            setupAutoCompleteTextField(it, binding.tvFleet)
        }

        viewModel.getAllSailors().observe(viewLifecycleOwner) {
            setupAutoCompleteTextField(it, binding.tvHelm)
            setupAutoCompleteTextField(it, binding.tvCrew)
        }
    }

    private fun setupAutoCompleteTextField(list: List<String>, view: AutoCompleteTextView) {
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            list
        )
        view.apply {
            setAdapter(arrayAdapter)
            setOnClickListener{
                this.showDropDown()
            }
            if(view == binding.boatClass) {
                setOnItemClickListener { parent, view, position, id ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        var number = viewModel.getNumberByClass(adapter.getItem(position).toString())
                        binding.etRating.setText(number.toString())
                    }
                }
            }
        }
    }

    private fun initView(boatInfo: BoatWithPYNumber) {
        binding.boatClass.setText(boatInfo.boat.boatClass)
        binding.etRating.setText(boatInfo.number.Number.toString())
        binding.sailNo.setText(boatInfo.boat.sailNo)
        binding.tvHelm.setText(boatInfo.boat.helm)
        binding.tvCrew.setText(boatInfo.boat.crew)
        binding.tvClub.setText(boatInfo.boat.club)
        binding.tvFleet.setText(boatInfo.boat.fleet)
    }

    override fun onSaveClick(boatInfo: BoatWithPYNumber?) {
        var boat: Boat
        var number: PYNumbers
        // Init to null, and reassign only if add new boat
        var overallResult: OverallResult? = null

        val boatClass = binding.boatClass.text.toString()
        val sailNo = binding.sailNo.text.toString()
        val helm = binding.tvHelm.text.toString()
        val crew = binding.tvCrew.text.toString()
        val club = binding.tvClub.text.toString()
        val fleet = binding.tvFleet.text.toString()
        // return 0 if get null
        val rating = binding.etRating.text.toString().toIntOrNull() ?:0

        // Check important fields
        if (boatClass.isNullOrEmpty() or sailNo.isNullOrEmpty() or (rating == 0)) {
            Snackbar.make(requireView(), "Please enter all fields with *", Snackbar.LENGTH_SHORT).show()
        } else {
            if (boatInfo == null) {
                // Add new boat
                boat = Boat(sailNo, boatClass, helm, crew, club, fleet)
                number = PYNumbers(boatClass, rating)
                overallResult = OverallResult(sailNo, viewModel.currentSeries.id)
                Toast.makeText(context, "New Boat Added", Toast.LENGTH_SHORT).show()
            } else {
                // Update current boat
                boat = boatInfo.boat
                number = boatInfo.number

                boat.boatClass = boatClass
                boat.sailNo = sailNo
                boat.helm = helm
                boat.crew = crew
                boat.club = club
                boat.fleet = fleet

                number.Number = rating
                Toast.makeText(context, "Update Saved", Toast.LENGTH_SHORT).show()
            }
            viewModel.upsertBoat(boat)
            viewModel.upsertPYNumber(number)
            overallResult?.let {
                // If add new boat, insert new overall result
                viewModel.upsertOverallResult(it)
            }
            findNavController().popBackStack()
        }
    }
}