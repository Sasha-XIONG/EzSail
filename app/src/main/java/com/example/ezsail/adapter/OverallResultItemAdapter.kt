package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.OverallItem
import com.example.ezsail.databinding.ItemOverallResultBinding

// Adapter for RecyclerView in the ViewPager
class OverallResultItemAdapter(private val overallResultList: List<OverallItem>):
    RecyclerView.Adapter<OverallResultItemAdapter.OverallResultItemHolder>() {

    // Generate ResultItemHolder object with item_overall_result layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OverallResultItemHolder {
        val binding = ItemOverallResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OverallResultItemHolder(binding)
    }

    // Bind data with view
    override fun onBindViewHolder(holder: OverallResultItemHolder, position: Int) {
        val OverallItem = overallResultList[position]
        holder.bindOverallItem(OverallItem)
    }

    override fun getItemCount() = overallResultList.size

    inner class OverallResultItemHolder(val binding: ItemOverallResultBinding) : RecyclerView.ViewHolder(binding.root) {

        private var resultItem: OverallItem? = null

        fun bindOverallItem(item: OverallItem) {
            resultItem = item
            binding.boatClass.text = item.boatClass
            binding.sailNumber.text = item.sailNumber
            binding.nett.text = item.nett.toString()
            binding.rank.text = item.rank.toString()
        }
    }
}