package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.dataclass.RegionData
import com.fitdev.myapplication.databinding.ItemHomeRegionBinding

class RegionViewAdapter(private val regionDataList : List<RegionData>) : RecyclerView.Adapter<RegionViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemHomeRegionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val regionData = regionDataList[position]
        holder.binding.regionName.text = regionData.regionName
        holder.binding.regionDestinationCount.text = regionData.destinationCount.toString() + " Destinations"
        holder.binding.regionImage.load("https://aplikasijpm.online/fitproject/pulau/" + regionData.imageName)
    }

    override fun getItemCount(): Int {
        return regionDataList.size
    }
}