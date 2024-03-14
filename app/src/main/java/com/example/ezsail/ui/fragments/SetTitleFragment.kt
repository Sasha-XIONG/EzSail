package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentSetTitleBinding
import com.example.ezsail.db.entities.Series
import com.example.ezsail.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetTitleFragment: Fragment() {

    private var setTitleBinding: FragmentSetTitleBinding? = null
    private val binding get() = setTitleBinding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTitleBinding = FragmentSetTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.okBtn.setOnClickListener {
            saveSeriesToDB()
        }

        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveSeriesToDB() {
        val seriesTitle = binding.edtTitle.text.toString().trim()

        if (seriesTitle.isNotEmpty()) {
            var series = Series(seriesTitle)
            viewModel.upsertSeries(series)
            Toast.makeText(this.context, "Series Saved", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_setTitleFragment_to_allSeriesFragment)
        } else {
            Snackbar.make(requireView(), "Please enter a title", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setTitleBinding = null
    }
}