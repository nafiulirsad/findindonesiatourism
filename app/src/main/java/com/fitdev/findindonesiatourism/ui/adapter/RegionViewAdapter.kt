package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.dataclass.RegionData
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.DetailsFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
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
        with(holder){
            this.binding.regionName.text = regionData.regionName
            this.binding.regionDestinationCount.text = regionData.destinationCount.toString() + " Destinations"
            this.binding.regionImage.load("https://aplikasijpm.online/fitproject/pulau/" + regionData.imageName)

            this.itemView.setOnClickListener{
                val regionKeyword: String? = regionData.regionName
                val activity = this.itemView.context as AppCompatActivity
                val exploreFragment = ExploreFragment()
                exploreFragment.arguments = Bundle().apply {
                    putString(ExploreFragment.ARG_SEARCH_PARAMS, regionKeyword)
                    putString(ExploreFragment.ARG_SEARCH_TYPE, "Region")
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, exploreFragment).addToBackStack(null).commit()
                activity.supportFragmentManager.beginTransaction().remove(HomeFragment()).commit()
                activity.supportFragmentManager.beginTransaction().remove(CategoryFragment()).commit()
                activity.supportFragmentManager.beginTransaction().remove(FavoriteFragment()).commit()
                activity.supportFragmentManager.beginTransaction().remove(DetailsFragment()).commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return regionDataList.size
    }
}