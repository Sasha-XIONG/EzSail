package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ezsail.R
import com.example.ezsail.TimingUtility
import com.example.ezsail.databinding.FragmentEditInfoBinding
import com.example.ezsail.databinding.FragmentSetTitleBinding
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.services.TimingService
import com.example.ezsail.ui.viewmodels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
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
            setOnItemClickListener { parent, view, position, id ->
                viewLifecycleOwner.lifecycleScope.launch {
                    Log.d("ttt", "${viewModel.getNumberByClass(adapter.getItem(position).toString())}")
                    var number = viewModel.getNumberByClass(adapter.getItem(position).toString())
                    binding.etRating.setText(number.toString())
                }
            }
        }
    }
}