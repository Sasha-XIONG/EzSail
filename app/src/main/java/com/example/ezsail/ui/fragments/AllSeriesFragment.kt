package com.example.ezsail.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezsail.R
import com.example.ezsail.adapter.SeriesListAdapter
import com.example.ezsail.databinding.FragmentAllSeriesBinding
import com.example.ezsail.db.entities.Series
import com.example.ezsail.listeners.AllSeriesEventListener
import com.example.ezsail.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class AllSeriesFragment:
    Fragment(R.layout.fragment_all_series),
    SearchView.OnQueryTextListener,
    AllSeriesEventListener {

    private lateinit var html: String
    private lateinit var resultLauncher: ActivityResultLauncher<String>

    // Set up view binding
    private var allSeriesBinding: FragmentAllSeriesBinding? = null
    private val binding get() = allSeriesBinding!!

    // Dagger manages viewmodel factories
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var seriesListAdapter: SeriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allSeriesBinding = FragmentAllSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup recyclerview
        setupAllSeriesRecyclerView()
        // Setup search view
        setupSearchView()

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_allSeriesFragment_to_setTitleFragment)
        }

        // Init activity result launcher
        // Called when return from new activity
        resultLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument()) {
            it?.let {
                writeToFile(it)
                Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show()
                val direction = AllSeriesFragmentDirections.actionAllSeriesFragmentToWebViewFragment(html)
                findNavController().navigate(direction)
            }
        }
    }

    private fun setupAllSeriesRecyclerView() {
        seriesListAdapter = SeriesListAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = seriesListAdapter
        }

        activity?.let {
            viewModel.getAllSeries().observe(viewLifecycleOwner) {
                seriesListAdapter.differ.submitList(it)
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.isSubmitButtonEnabled = false
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun searchSeries(title: String?) {
        // % indicates the query string can be in any place of the title
        val searchQuery = "%$title%"

        viewModel.searchSeries(searchQuery).observe(this) {
            seriesListAdapter.differ.submitList(it)
        }
    }

    // Search by click on search button
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    // Results show up as user typing
    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            searchSeries(it)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        // When this fragment is not in use, set the binding to null
        allSeriesBinding = null
    }

    override fun onDelete(series: Series) {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Series")
            setMessage("Do you want to delete this series?")
            setPositiveButton("Delete") {_,_ ->
                viewModel.deleteSeries(series)
                Toast.makeText(context, "Series Deleted", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onPublish(series: Series) {
        viewLifecycleOwner.lifecycleScope.launch{
            // Get html file
            html = viewModel.publish(series)
            // Save html file
            resultLauncher.launch("result.html")
        }
    }

    // Function to write html file to empty file
    private fun writeToFile(uri: Uri)  {
        try {
            val pfd = requireContext().contentResolver.openFileDescriptor(uri, "w")
            val fos = FileOutputStream(pfd?.fileDescriptor)
            fos.write(html.toByteArray())
            fos.close()
            pfd?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}