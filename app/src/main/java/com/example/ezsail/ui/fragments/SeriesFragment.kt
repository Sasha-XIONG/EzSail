package com.example.ezsail.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.ezsail.R
import com.example.ezsail.TimingUtility
import com.example.ezsail.adapter.SeriesViewPagerAdapter
import com.example.ezsail.databinding.FragmentSeriesBinding
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.Series
import com.example.ezsail.ui.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Fragment shows navigation bar and view pager
@AndroidEntryPoint
class SeriesFragment:
    Fragment(R.layout.fragment_series),
    SearchView.OnQueryTextListener {

    // Dagger manages viewmodel factories
    private val viewModel: MainViewModel by activityViewModels()

    // Set up view binding
    private var seriesBinding: FragmentSeriesBinding? = null
    private val binding get() = seriesBinding!!

    lateinit var mAdapter: SeriesViewPagerAdapter

    private lateinit var currentSeries: Series
    private val args: SeriesFragmentArgs by navArgs()

    private val viewPagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            // Update race date as the page selected changing
            if (position == 0) {
                requireActivity().findViewById<TextView>(R.id.tv_dateTitle).visibility = View.GONE
                requireActivity().findViewById<TextView>(R.id.tv_raceDate).visibility = View.GONE
            } else {
                // TODO: SETPOSITION
//                viewModel.position = position
                requireActivity().findViewById<TextView>(R.id.tv_dateTitle).visibility = View.VISIBLE
                requireActivity().findViewById<TextView>(R.id.tv_raceDate).apply {
                    visibility = View.VISIBLE
                    val timestamp = mAdapter.getFragment(position).race.timestamp
                    text = TimingUtility.getFormattedDate(timestamp)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        seriesBinding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentSeries = args.series!!
        viewModel.currentSeries = currentSeries
        // Set series title to tool bar title
        requireActivity().findViewById<EditText>(R.id.title).setText(currentSeries.title)

        // Setup title onchange listener
        setupTitleListener()

        mAdapter = SeriesViewPagerAdapter(this)

        // Initialise viewPager
        binding.viewPager.apply {
            adapter = mAdapter
            this.offscreenPageLimit = 10
            registerOnPageChangeCallback(viewPagerChangeCallback)
        }

        // Bind TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setCustomView(
                getTabView(mAdapter.getPageTitle(position).toString(), position)
            )
        }.attach()

        // Initialise fragments
        setupViewPagerAdapter()

        // Setup addRace button
        binding.addRaceBtn.setOnClickListener {
            // Get last fragment of the fragment list
            val lastFragment = mAdapter.getLastFragment()
            val currentTitle = if (lastFragment == null) {
                "1"
            } else {
                (lastFragment.race.raceNo+1).toString()
            }

            mAdapter.addFragment(
                RaceResultListFragment.getInstance(
                    currentTitle,
                    saveRaceToDB(currentTitle.toInt())),
                "R$currentTitle"
            )
            mAdapter.notifyDataSetChanged()
            // Set current showing page
            binding.viewPager.setCurrentItem(mAdapter.itemCount, true)
        }
    }

    // Init search view
    private fun setupSearchView() {
        binding.searchView.isSubmitButtonEnabled = false
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun searchSeries(sailNo: String?) {
        // % indicates the query string can be in any place of the title
        val searchQuery = "%$sailNo%"

        viewModel.searchBoatBySailNoAtOverallPage(searchQuery).observe(viewLifecycleOwner) {
            val frag = mAdapter.getFragment(0) as OverallResultListFragment
            val adapter = frag.getAdapter()
            adapter.differ.submitList(it)
        }
    }

    // Search by click on search button
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    // Results show up as user typing
    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            searchSeries(newText)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        seriesBinding = null
    }

    // Set customised tab item layout
    private fun getTabView(string: String, index: Int): View {
        val view = LayoutInflater.from(getContext()).inflate(R.layout.closeable_tab, null)

        // Set textView of tab item
        val textView = view.findViewById<TextView>(R.id.tabText)
        textView.setText(string)

        // Set onClickListener for closeBtn
        val closeBtn = view.findViewById<ImageButton>(R.id.closeBtn)
        if (index == 0) {
            // Overall page cannot be deleted
            closeBtn.visibility = View.GONE
        } else {
            closeBtn.setOnClickListener {
                // Alert for delete
                AlertDialog.Builder(activity).apply {
                    setTitle("Delete Race")
                    setMessage("Do you want to delete this race?")
                    setPositiveButton("Delete") {_,_ ->
                        val deleteFrag = mAdapter.deleteFragment(index).second
                        mAdapter.notifyDataSetChanged()
                        // Delete race from database
                        viewModel.deleteRace(deleteFrag.race)
                        Toast.makeText(context, "Race Deleted", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Cancel", null)
                }.create().show()
            }
        }
        return view;
    }

    // Enable to change title
    private fun setupTitleListener() {
        val title = requireActivity().findViewById<EditText>(R.id.title)
        val oldTitle = title.text.toString()

        title.setOnFocusChangeListener {_, hasFocus ->
            if (hasFocus) {
                title.isCursorVisible = true
            } else {
                title.isCursorVisible = false
                val newTitle = title.text.toString().trim()
                if(newTitle.isNotEmpty()) {
                    currentSeries.title = newTitle
                    viewModel.upsertSeries(currentSeries)
                    Toast.makeText(context, "Title Changed", Toast.LENGTH_SHORT).show()
                } else {
                    title.setText(oldTitle)
                    Toast.makeText(context, "Title connot be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }

        title.setOnEditorActionListener{_, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                title.clearFocus()
            }
            false
        }
    }

    // Save race to database and return the race
    private fun saveRaceToDB(raceNo: Int): Race {
        val seriesId = currentSeries.id
        val raceNo = raceNo
        val dateTimestamp = Calendar.getInstance().timeInMillis
        // Initialise is_ongoing to true
        val isOngoing = true
        val race = Race(seriesId, raceNo, dateTimestamp, isOngoing)

        viewModel.upsertRace(race)
        return race
    }

    private fun setupViewPagerAdapter() {
        mAdapter.addFragment(
            OverallResultListFragment.getInstance("overall"),
            "Overall"
        )
        mAdapter.notifyDataSetChanged()

        // Call from background
        viewLifecycleOwner.lifecycleScope.launch {
            val raceList = viewModel.getAllRacesOfSeries(currentSeries.id)
            raceList?.forEach {
                mAdapter.addFragment(
                    RaceResultListFragment.getInstance(it.raceNo.toString(), it),
                    "R"+it.raceNo
                )
            }
            mAdapter.notifyDataSetChanged()
        }
    }

}