package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.databinding.ItemOverallResultBinding
import com.example.ezsail.db.entities.relations.OverallResultsWithBoat

// Adapter for RecyclerView in the ViewPager
class OverallResultItemAdapter():
    RecyclerView.Adapter<OverallResultItemAdapter.OverallResultViewHolder>() {

    class OverallResultViewHolder(val itemBinding: ItemOverallResultBinding):
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<OverallResultsWithBoat>(){
        override fun areItemsTheSame(oldItem: OverallResultsWithBoat, newItem: OverallResultsWithBoat): Boolean {
            return oldItem.boat == newItem.boat &&
                    oldItem.overallResult == newItem.overallResult
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
}