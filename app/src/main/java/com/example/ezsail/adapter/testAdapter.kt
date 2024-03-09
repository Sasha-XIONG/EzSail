package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.OverallItem
import com.example.ezsail.databinding.ItemOverallResultBinding
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.db.entities.relations.OverallResultsWithBoat

// Adapter for RecyclerView in the ViewPager
class testAdapter():
    RecyclerView.Adapter<testAdapter.OverallResultViewHolder>() {

    class OverallResultViewHolder(val itemBinding: ItemOverallResultBinding):
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<OverallResultsWithBoat>(){
        override fun areItemsTheSame(oldItem: OverallResultsWithBoat, newItem: OverallResultsWithBoat): Boolean {
            return oldItem.boat == newItem.boat &&
                    oldItem.overallResult == newItem.overallResult
//                    oldItem.seriesId == newItem.seriesId
        }

        override fun areContentsTheSame(oldItem: OverallResultsWithBoat, newItem: OverallResultsWithBoat): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverallResultViewHolder {
        val itemBinding = ItemOverallResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OverallResultViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OverallResultViewHolder, position: Int) {
        val currentOverallResult = differ.currentList[position]
        holder.itemBinding.sailNumber.text = currentOverallResult.boat.sailNo
        holder.itemBinding.boatClass.text = currentOverallResult.boat.boatClass
        holder.itemBinding.nett.text = currentOverallResult.overallResult.nett.toString()
        holder.itemBinding.rank.text = null
    }

//    // Generate ResultItemHolder object with item_overall_result layout
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): OverallResultItemHolder {
//        val binding = ItemOverallResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return OverallResultItemHolder(binding)
//    }
//
//    // Bind data with view
//    override fun onBindViewHolder(holder: OverallResultItemHolder, position: Int) {
//        val OverallItem = overallResultList[position]
//        holder.bindOverallItem(OverallItem)
//    }
//
//    override fun getItemCount() = overallResultList.size
//
//    inner class OverallResultItemHolder(val binding: ItemOverallResultBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        private var resultItem: OverallItem? = null
//
//        fun bindOverallItem(item: OverallItem) {
//            resultItem = item
//            binding.boatClass.text = item.boatClass
//            binding.sailNumber.text = item.sailNumber
//            binding.nett.text = item.nett.toString()
//            binding.rank.text = item.rank.toString()
//        }
//    }
}