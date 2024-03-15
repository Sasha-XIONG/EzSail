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

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AllSeriesFragment:
    Fragment(R.layout.fragment_all_series),
    SearchView.OnQueryTextListener,
    AllSeriesEventListener {

    private lateinit var html: String
    lateinit var series: Series

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
    }

    // Called when return from the new intent

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        writeToFile(data?.data!!)
        Toast.makeText(requireContext(), "File Saved", Toast.LENGTH_SHORT).show()
        val direction = AllSeriesFragmentDirections.actionAllSeriesFragmentToWebViewFragment(html)
        findNavController().navigate(direction)
    }

    private fun setupAllSeriesRecyclerView() {
        seriesListAdapter = SeriesListAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = seriesListAdapter
        }

        activity?.let {
            viewModel.getAllSeries().observe(viewLifecycleOwner) {series ->
                seriesListAdapter.differ.submitList(series)
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

        viewModel.searchSeries(searchQuery).observe(this) { resultList ->
            seriesListAdapter.differ.submitList(resultList)
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
            delay(500)
            // Save html file
            saveFile(series.title)
//            val direction = AllSeriesFragmentDirections.actionAllSeriesFragmentToWebViewFragment(viewModel.html)
//            findNavController().navigate(direction)
        }
    }

    // Function to create an empty file
    private fun saveFile(title: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.apply {
            type = "text/html"
            putExtra(Intent.EXTRA_TITLE, "${title}.html")
            startActivityForResult(this, 40)
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