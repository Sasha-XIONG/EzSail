package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentEditInfoBinding
import com.example.ezsail.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class InputInfoFragment: Fragment(R.layout.fragment_edit_info) {
    // Set up view binding
    private var editInfoBinding: FragmentEditInfoBinding? = null
    private val binding get() = editInfoBinding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editInfoBinding = FragmentEditInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init adapter for auto-complete text field
        subscribeToObservers()
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
}