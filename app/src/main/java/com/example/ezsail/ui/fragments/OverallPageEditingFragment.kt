package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentOverallPageEditingBinding
import com.example.ezsail.db.entities.relations.BoatWithPYNumber
import com.example.ezsail.listeners.BoatInfoEditingEventListener

class OverallPageEditingFragment: Fragment(R.layout.fragment_overall_page_editing) {
    private var editingBinding: FragmentOverallPageEditingBinding? = null
    private val binding get() = editingBinding!!

    private lateinit var listener: BoatInfoEditingEventListener

    private lateinit var boatInfo: BoatWithPYNumber

    private val args: OverallPageEditingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editingBinding = FragmentOverallPageEditingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boatInfo = args.boatInfoWithOverallResult!!.boatWithPYNumber

        // Init child fragment
        val editFragment = InputInfoFragment(boatInfo)

        childFragmentManager.beginTransaction()
            .replace(R.id.fragContainer, editFragment, "overallEditFrag")
            .addToBackStack("overallEditFrag")
            .commit()

        // Init listener
        listener = editFragment

        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            listener.onSaveClick(boatInfo)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editingBinding = null
    }
}