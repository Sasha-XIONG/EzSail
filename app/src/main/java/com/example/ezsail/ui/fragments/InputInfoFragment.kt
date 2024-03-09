package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentEditInfoBinding
import com.example.ezsail.databinding.FragmentSetTitleBinding
import com.example.ezsail.db.entities.Boat

class InputInfoFragment: Fragment(R.layout.fragment_edit_info) {
    // Set up view binding
    private var editInfoBinding: FragmentEditInfoBinding? = null
    private val binding get() = editInfoBinding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Initialise boat
        val boatClass = binding.boatClass.text.toString()
        val sailNo = binding.sailNo.text.toString()
        val helm = binding.helm.text.toString()
        val crew = binding.crew.text.toString()
        val club = binding.club.text.toString()
        val fleet = binding.fleet.text.toString()

        val boat = Boat(sailNo, boatClass, helm, crew, club, fleet)
    }
}