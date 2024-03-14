package com.example.ezsail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.R
import com.example.ezsail.databinding.ItemRaceResultBinding
import com.example.ezsail.db.entities.Race
import com.example.ezsail.db.entities.relations.RaceResultsWithBoatAndPYNumber
import com.example.ezsail.listeners.RaceResultEventListener

// Adapter for RecyclerView in the ViewPager
class RaceResultItemAdapter(listener: RaceResultEventListener, val race: Race, val context: Context):
    RecyclerView.Adapter<RaceResultItemAdapter.RaceResultViewHolder>() {

    private val listener = listener

    // Adapter for spinner
    private val arrayAdapter = ArrayAdapter.createFromResource(
        context,
        R.array.codes,
        android.R.layout.simple_spinner_dropdown_item
    )

    class RaceResultViewHolder(val itemBinding: ItemRaceResultBinding):
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RaceResultsWithBoatAndPYNumber>(){
        override fun areItemsTheSame(oldItem: RaceResultsWithBoatAndPYNumber, newItem: RaceResultsWithBoatAndPYNumber): Boolean {
            return oldItem.boatWithPYNumber == newItem.boatWithPYNumber &&
                    oldItem.raceResult == newItem.raceResult
        }

        override fun areContentsTheSame(oldItem: RaceResultsWithBoatAndPYNumber, newItem: RaceResultsWithBoatAndPYNumber): Boolean {
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
        holder.itemBinding.sailNumber.text = currentRaceResult.boatWithPYNumber.boat.sailNo
        holder.itemBinding.boatClass.text = currentRaceResult.boatWithPYNumber.boat.boatClass
        holder.itemBinding.laps.text = currentRaceResult.raceResult.laps.toString()
        // If race is not ongoing, show hide finish button and show elapsed time
        if (!race.is_ongoing) {
            holder.itemBinding.finishBtn.visibility = View.GONE
            holder.itemBinding.unpinBtn.visibility = View.GONE
            holder.itemBinding.pinBtn.visibility = View.GONE
            holder.itemBinding.addLapBtn.visibility = View.GONE
            holder.itemBinding.points.apply {
                visibility = View.VISIBLE
                text = currentRaceResult.raceResult.points.toString()
            }
            holder.itemBinding.elapsedTime.apply {
                visibility = View.VISIBLE
                text = currentRaceResult.raceResult.elapsedTime.toString()
            }
        }

        holder.itemBinding.codeSpinner.apply {
            adapter = arrayAdapter
            setSelection(currentRaceResult.raceResult.code)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    listener.onCodeSelect(currentRaceResult.raceResult, position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

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