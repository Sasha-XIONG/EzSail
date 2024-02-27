package com.example.ezsail.ui.fragments.newcompetition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.ezsail.R
import com.example.ezsail.adapter.RecordResultAdapter
import com.example.ezsail.databinding.CloseableTabBinding
import com.example.ezsail.databinding.FragmentRecordResultBinding
import com.example.ezsail.databinding.ItemRaceResultBinding
import com.example.ezsail.ui.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

// Fragment shows navigation bar and view pager
@AndroidEntryPoint
class RecordResultFragment: Fragment(R.layout.fragment_record_result) {

    // Dagger manages viewmodel factories
    // TODO: correct view model needed
    private val viewModel: MainViewModel by viewModels()

    // Set up view binding
    private var _binding: FragmentRecordResultBinding? = null
    private val binding get() = _binding!!

    lateinit var mAdapter: RecordResultAdapter

    private val viewPagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = RecordResultAdapter(this)

        // Initialise adapter for viewPager
        binding.viewPager.adapter = mAdapter

        // Enable turning page
        binding.viewPager.registerOnPageChangeCallback(viewPagerChangeCallback)

        // Bind TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setCustomView(
                getTabView(mAdapter.getPageTitle(position).toString(), position)
            )
        }.attach()

        // Set the initial fragment (overall page)
        mAdapter.addFragment(
            OverallResultListFragment.getInstance("overall"),
            "Overall")
        mAdapter.notifyDataSetChanged()
        binding.viewPager.setCurrentItem(mAdapter.itemCount, true)

        // Setup addRace button
        binding.addRaceBtn.setOnClickListener {
            mAdapter.addFragment(
                RaceResultListFragment.getInstance((mAdapter.itemCount).toString()),
                "R"+(mAdapter.itemCount).toString())
            mAdapter.notifyDataSetChanged()
            binding.viewPager.setCurrentItem(mAdapter.itemCount, true)
        }
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
                mAdapter.deleteFragment(index)
                mAdapter.notifyDataSetChanged()
            }
        }
        return view;
    }
}