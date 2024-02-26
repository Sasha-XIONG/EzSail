package com.example.ezsail.ui.fragments.newcompetition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentSetTitleBinding
import com.example.ezsail.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetTitleFragment: Fragment(R.layout.fragment_set_title) {

    // Dagger manages viewmodel factories
    // TODO: correct view model needed
    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentSetTitleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetTitleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.okBtn.setOnClickListener {
            findNavController().navigate(R.id.action_setTitleFragment_to_recordResultFragment)
        }
    }
}