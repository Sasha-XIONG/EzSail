package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentEditResultBinding
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.listeners.RaceResultEditingEventListener
import com.example.ezsail.ui.viewmodels.MainViewModel

class InputRaceResultFragment(
    private val raceResult: RaceResult
): Fragment(R.layout.fragment_edit_result), RaceResultEditingEventListener {
    private var editResultBinding: FragmentEditResultBinding? = null
    private val binding get() = editResultBinding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editResultBinding = FragmentEditResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init view for editing page
        initView(raceResult)
    }

    private fun initView(raceResult: RaceResult) {
        binding.etLaps.setText(raceResult.laps.toString())
        binding.etElapsedTime.setText(raceResult.elapsedTime.toString())
    }

    override fun onSaveClick(raceResult: RaceResult) {
        // Check if view is created
        view?.apply {
            raceResult.laps = binding.etLaps.text.toString().toInt()
            raceResult.elapsedTime = binding.etElapsedTime.text.toString().toFloatOrNull()
            viewModel.updateRaceResult(raceResult)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editResultBinding = null
    }
}