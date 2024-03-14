package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentAddCompetitiorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCompetitorFragment: Fragment(R.layout.fragment_add_competitior) {
    // Set up view binding
    private var addCompetitiorBinding: FragmentAddCompetitiorBinding? = null
    private val binding get() = addCompetitiorBinding!!

    private lateinit var listener: InputInfoFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addCompetitiorBinding = FragmentAddCompetitiorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init listener
        listener = this.childFragmentManager.findFragmentById(R.id.inputFragment) as InputInfoFragment

        // Set onClickListener for save button
        binding.saveBtn.setOnClickListener {
            listener.onSaveClick()
        }

        // Set onClickListener for cancel button
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addCompetitiorBinding = null
    }
}