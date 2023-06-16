package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem
import com.fitdev.myapplication.databinding.ItemRowExploreBinding

class ExploreViewAdapter(private val exploreDataList : List<ResultsItem?>?) : RecyclerView.Adapter<ExploreViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRowExploreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val popularData = exploreDataList?.get(position)
        with(holder){
            this.binding.exploreName.text = popularData?.name
            binding.exploreName.movementMethod = ScrollingMovementMethod()
            this.binding.exploreReviewCount.text = "${popularData?.userRatingsTotal} Reviews"
            this.binding.exploreRating.rating = popularData?.rating.toString().toFloat()
            this.binding.exploreImage.load(photoUrl(popularData?.photos?.get(0)?.photoReference))
        }
    }

    override fun getItemCount(): Int {
        return exploreDataList?.size ?: 0
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