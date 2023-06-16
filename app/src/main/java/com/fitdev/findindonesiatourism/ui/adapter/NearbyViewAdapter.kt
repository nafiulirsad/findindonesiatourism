package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch.ResultsItem
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ItemHomeNearbyBinding

class NearbyViewAdapter(private val nearbyDataList : List<ResultsItem?>?) : RecyclerView.Adapter<NearbyViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemHomeNearbyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeNearbyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nearbyData = nearbyDataList?.get(position)
        with(holder){
            val activity = this.itemView.context as AppCompatActivity
            this.binding.nearbyName.text = nearbyData?.name
            this.binding.nearbyVillage.text = nearbyData?.vicinity
            this.binding.nearbyImage.load(photoUrl(nearbyData?.photos?.get(0)?.photoReference))

            this.itemView.setOnClickListener{
                val regionKeyword: String? = nearbyDataList?.get(position)?.name
                val exploreFragment = ExploreFragment()
                exploreFragment.arguments = Bundle().apply {
                    putString(ExploreFragment.ARG_REGION_NAME, regionKeyword)
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, exploreFragment).addToBackStack(null).commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return nearbyDataList?.size ?: 0
    }

    private fun photoUrl(photoReference: String?): String{
        val key = "AIzaSyBl416wxXDeyiRk3ZuTsLXFjRhx_1e_QXg"
        val maxheight = "250"
        return if(photoReference.isNullOrEmpty()){
            "https://aplikasijpm.online/fitproject/default/defaultimage.png"
        } else{
            "https://maps.googleapis.com/maps/api/place/photo?key=$key&photo_reference=$photoReference&maxheight=$maxheight"
        }
    }
}