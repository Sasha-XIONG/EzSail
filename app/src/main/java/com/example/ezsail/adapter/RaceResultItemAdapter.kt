package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.databinding.ItemRaceResultBinding
import com.example.ezsail.db.entities.relations.RaceResultsWithBoat

// Adapter for RecyclerView in the ViewPager
class RaceResultItemAdapter():
    RecyclerView.Adapter<RaceResultItemAdapter.RaceResultViewHolder>() {

    class RaceResultViewHolder(val itemBinding: ItemRaceResultBinding):
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RaceResultsWithBoat>(){
        override fun areItemsTheSame(oldItem: RaceResultsWithBoat, newItem: RaceResultsWithBoat): Boolean {
            return oldItem.boat == newItem.boat &&
                    oldItem.raceResult == newItem.raceResult
        }

        override fun areContentsTheSame(oldItem: RaceResultsWithBoat, newItem: RaceResultsWithBoat): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceResultViewHolder {
        val itemBinding = ItemRaceResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RaceResultViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RaceResultViewHolder, position: Int) {
        val currentRaceResult = differ.currentList[position]

        holder.itemBinding.sailNumber.text = currentRaceResult.boat.sailNo
        holder.itemBinding.boatClass.text = currentRaceResult.boat.boatClass
        holder.itemBinding.laps.text = currentRaceResult.raceResult.laps.toString()
        holder.itemBinding.code.text = null
    }
}