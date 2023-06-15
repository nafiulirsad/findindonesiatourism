package com.fitdev.findindonesiatourism.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.dataclass.popular.PopularAttractionsItem
import com.fitdev.myapplication.databinding.ItemHomePopularBinding

class PopularViewAdapter(private val popularDataList : List<PopularAttractionsItem>) : RecyclerView.Adapter<PopularViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemHomePopularBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomePopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val popularData = popularDataList[position]
        holder.binding.popularName.text = popularData.name
        holder.binding.popularRating.rating = popularData.rating.toString().toFloat()
        holder.binding.popularImage.load(popularData.photoUrl)
    }

    override fun getItemCount(): Int {
        return popularDataList.size
    }
}