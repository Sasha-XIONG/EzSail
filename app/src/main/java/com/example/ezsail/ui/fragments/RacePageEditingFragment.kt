package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.ezsail.R
import com.example.ezsail.adapter.EditPageViewPagerAdapter
import com.example.ezsail.databinding.FragmentRacePageEditingBinding
import com.example.ezsail.db.entities.relations.RaceResultsWithBoatAndPYNumber
import com.example.ezsail.listeners.BoatInfoEditingEventListener
import com.example.ezsail.listeners.RaceResultEditingEventListener
import com.google.android.material.tabs.TabLayoutMediator

class RacePageEditingFragment: Fragment(R.layout.fragment_race_page_editing) {
    private var editingBinding: FragmentRacePageEditingBinding? = null
    private val binding get() = editingBinding!!

    private lateinit var boatInfoListener: BoatInfoEditingEventListener
    private lateinit var raceResultListener: RaceResultEditingEventListener

    private lateinit var mAdapter: EditPageViewPagerAdapter

    private lateinit var boatInfoWithResult: RaceResultsWithBoatAndPYNumber
    private val args: RacePageEditingFragmentArgs by navArgs()

    private val viewPagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    private lateinit var tabArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editingBinding = FragmentRacePageEditingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabArray = resources.getStringArray(R.array.tabs)

        boatInfoWithResult = args.boatInfoWithRaceResult!!

        // Init view pager
        mAdapter = EditPageViewPagerAdapter(this, tabArray, boatInfoWithResult)
        binding.editViewPager.adapter = mAdapter
        binding.editViewPager.registerOnPageChangeCallback(viewPagerChangeCallback)

        boatInfoListener = mAdapter.getFragment(0) as InputInfoFragment
        raceResultListener = mAdapter.getFragment(1) as InputRaceResultFragment

        // Init tablayout
        TabLayoutMediator(binding.editTabLayout, binding.editViewPager) {tab, position ->
            tab.text = tabArray[position]
        }.attach()

        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveBtn.setOnClickListener {
            boatInfoListener.onSaveClick(boatInfoWithResult.boatWithPYNumber)
            raceResultListener.onSaveClick(boatInfoWithResult.raceResult)
        }
    }
}