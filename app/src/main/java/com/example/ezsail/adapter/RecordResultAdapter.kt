package com.example.ezsail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ezsail.R
import com.example.ezsail.databinding.CloseableTabBinding
import java.util.concurrent.atomic.AtomicLong

// Adapter for ViewPager2
class RecordResultAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    // Arraylist for fragments
    private var mFragmentList = ArrayList<Fragment>()

    // Arraylist for tab titles
    // TODO: CREATE A CLASS FOR TAB BUTTONS
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
    fun addFragment(fragment: Fragment, title: String): RecordResultAdapter {
        fragment?.let { frag ->
            mFragmentList.add(frag)
            mTitleList.add(title)
            mIds.add(getAtomicGeneratedId())
        }
        return this
    }

    // Delete race
    fun deleteFragment(index: Int): RecordResultAdapter {
        mFragmentList.removeAt(index)
        mTitleList.removeAt(index)
        mIds.removeAt(index)
        return this
    }

    fun getPageTitle(position: Int): CharSequence {
        return mTitleList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return mIds.get(position)
    }
}