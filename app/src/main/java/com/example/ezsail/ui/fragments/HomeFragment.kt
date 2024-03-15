package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
    // Set up view binding
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allSeriesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allSeriesFragment)
        }

        binding.newSeriesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_setTitleFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }
}