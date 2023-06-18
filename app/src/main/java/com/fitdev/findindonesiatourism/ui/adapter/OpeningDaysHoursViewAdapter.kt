package com.fitdev.findindonesiatourism.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitdev.myapplication.databinding.ItemOpeningDaysAndHoursBinding

class OpeningDaysHoursViewAdapter(private val openingHoursDaysList : List<String?>?) : RecyclerView.Adapter<OpeningDaysHoursViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemOpeningDaysAndHoursBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOpeningDaysAndHoursBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val openingHoursDaysData = openingHoursDaysList?.get(position)
        with(holder){
            this.binding.openingDaysHoursValue.text = openingHoursDaysData
        }
    }

    override fun getItemCount(): Int {
        return openingHoursDaysList?.size ?: 0
    }
}