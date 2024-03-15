package com.example.ezsail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ezsail.databinding.ItemSeriesBinding
import com.example.ezsail.db.entities.Series
import com.example.ezsail.listeners.AllSeriesEventListener
import com.example.ezsail.ui.fragments.AllSeriesFragmentDirections

// Adapter for RecyclerView of all competitions
class SeriesListAdapter(listener: AllSeriesEventListener):
    RecyclerView.Adapter<SeriesListAdapter.SeriesViewHolder>(){

     val listener = listener

    // View Holder
    class SeriesViewHolder(val itemBinding: ItemSeriesBinding):
        RecyclerView.ViewHolder(itemBinding.root)

    // Update recycler without refreshing the whole dataset
    private val differCallback = object : DiffUtil.ItemCallback<Series>(){
        override fun areItemsTheSame(oldItem: Series, newItem: Series): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Series, newItem: Series): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val itemBinding = ItemSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeriesViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val currentSeries = differ.currentList[position]
        holder.itemBinding.seriesTitle.text = currentSeries.title

        holder.itemBinding.publishBtn.setOnClickListener {
            listener.onPublish(currentSeries)
        }

        holder.itemBinding.deleteBtn.setOnClickListener {
            listener.onDelete(currentSeries)
        }

        holder.itemView.setOnClickListener {
            val direction = AllSeriesFragmentDirections.actionAllSeriesFragmentToSeriesFragment(currentSeries)
            it.findNavController().navigate(direction)
        }
    }
}