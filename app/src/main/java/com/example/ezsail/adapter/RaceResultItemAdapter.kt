package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.databinding.ItemRaceResultBinding
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.relations.RaceResultsWithBoat
import com.example.ezsail.listeners.RaceResultEventListener

// Adapter for RecyclerView in the ViewPager
class RaceResultItemAdapter(listener: RaceResultEventListener, race: Race):
    RecyclerView.Adapter<RaceResultItemAdapter.RaceResultViewHolder>() {

    val listener = listener
    val race = race

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

        // Init race result item
        holder.itemBinding.sailNumber.text = currentRaceResult.boat.sailNo
        holder.itemBinding.boatClass.text = currentRaceResult.boat.boatClass
        holder.itemBinding.laps.text = currentRaceResult.raceResult.laps.toString()
        holder.itemBinding.code.text = currentRaceResult.raceResult.code
        // If race is not ongoing, show hide finish button and show elapsed time
        if (!race.is_ongoing) {
            holder.itemBinding.finishBtn.visibility = View.GONE
            holder.itemBinding.unpinBtn.visibility = View.GONE
            holder.itemBinding.pinBtn.visibility = View.GONE
            holder.itemBinding.points.apply {
                visibility = View.VISIBLE
                text = currentRaceResult.raceResult.points.toString()
            }
            holder.itemBinding.elapsedTime.apply {
                visibility = View.VISIBLE
                text = currentRaceResult.raceResult.elapsedTime.toString()
            }
        }

        holder.itemBinding.addLapBtn.setOnClickListener {
            listener.onAddLaps(currentRaceResult.raceResult)
            holder.itemBinding.laps.text = currentRaceResult.raceResult.laps.toString()
        }

        holder.itemBinding.finishBtn.setOnClickListener {
            listener.onFinish(currentRaceResult.raceResult)
            // Hide finish button
            holder.itemBinding.finishBtn.visibility = View.GONE
            // Show elapsed time
            holder.itemBinding.elapsedTime.apply {
                visibility = View.VISIBLE
                text = currentRaceResult.raceResult.elapsedTime.toString()
            }
        }

        holder.itemBinding.unpinBtn.setOnClickListener {
            holder.itemBinding.unpinBtn.visibility = View.GONE
            holder.itemBinding.pinBtn.visibility = View.VISIBLE

            var cList = differ.currentList.toMutableList()
            cList.remove(currentRaceResult)
            cList = (mutableListOf(currentRaceResult) + cList).toMutableList()
            differ.submitList(cList)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentRaceResult)
        }
    }
}