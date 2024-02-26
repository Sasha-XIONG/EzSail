package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.ResultItem
import com.example.ezsail.databinding.ItemRaceResultBinding

// Adapter for RecyclerView in the ViewPager
class RaceResultItemAdapter(private val raceResultList: List<ResultItem>):
    RecyclerView.Adapter<RaceResultItemAdapter.ResultItemHolder>() {

    // Generate ResultItemHolder object with item_race_result layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultItemHolder {
        val binding = ItemRaceResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultItemHolder(binding)
    }

    // Bind data with view
    override fun onBindViewHolder(holder: ResultItemHolder, position: Int) {
        val resultItem = raceResultList[position]
        holder.bindResultItem(resultItem)
    }

    override fun getItemCount() = raceResultList.size

    inner class ResultItemHolder(val binding: ItemRaceResultBinding) : RecyclerView.ViewHolder(binding.root) {

        private var resultItem: ResultItem? = null

        fun bindResultItem(item: ResultItem) {
            resultItem = item
            binding.boatClass.text = item.boatClass
            binding.sailNumber.text = item.sailNumber
            binding.laps.text = item.laps.toString()
        }
    }
}