package com.example.ezsail.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ezsail.ui.fragments.RaceResultListFragment
import java.util.concurrent.atomic.AtomicLong

// Adapter for ViewPager2
class SeriesViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    // Arraylist for fragments
    private var mFragmentList = ArrayList<Fragment>()

    // Arraylist for tab titles
    private var mTitleList = ArrayList<String>()

    // Arraylist for ids
    private var mIds = ArrayList<Long>()

    // Automatically generate long, used for race id
    private var mAtomicLong = AtomicLong(0)

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    // Show fragments
    override fun createFragment(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    // Get automatically generated race ids
    private fun getAtomicGeneratedId(): Long {
        return mAtomicLong.incrementAndGet()
    }

    // Add new race
    fun addFragment(fragment: Fragment, title: String): SeriesViewPagerAdapter {
        fragment?.let { frag ->
            mFragmentList.add(frag)
            mTitleList.add(title)
            mIds.add(getAtomicGeneratedId())
        }
        return this
    }

    // Delete race
    fun deleteFragment(index: Int): Pair<SeriesViewPagerAdapter, RaceResultListFragment> {
        val removeFragment = mFragmentList[index] as RaceResultListFragment
        mFragmentList.removeAt(index)
        mTitleList.removeAt(index)
        mIds.removeAt(index)
        return Pair(this, removeFragment)
    }

    fun getPageTitle(position: Int): CharSequence {
        return mTitleList.get(position)
    }

    fun getLastFragment(): RaceResultListFragment? {
        if (mFragmentList.size == 1) {
            return null
        } else {
            return mFragmentList.last() as RaceResultListFragment
        }
    }

    fun getFragment(index: Int): RaceResultListFragment {
        return mFragmentList[index] as RaceResultListFragment
    }
}