package com.example.ezsail.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ezsail.db.entities.relations.RaceResultsWithBoatAndPYNumber
import com.example.ezsail.ui.fragments.InputInfoFragment
import com.example.ezsail.ui.fragments.InputRaceResultFragment

class EditPageViewPagerAdapter(
    fragment: Fragment,
    private val items: Array<String>,
    raceResultsWithBoatAndPYNumber: RaceResultsWithBoatAndPYNumber
): FragmentStateAdapter(fragment) {

    private val mFragmentList = arrayListOf(
        InputInfoFragment(raceResultsWithBoatAndPYNumber.boatWithPYNumber),
        InputRaceResultFragment(raceResultsWithBoatAndPYNumber.raceResult)
    )

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun getFragment(index: Int): Fragment {
        return mFragmentList[index]
    }
}