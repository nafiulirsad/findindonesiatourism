package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.DetailsFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ItemRowExploreBinding

class ExploreViewAdapter(private val exploreDataList : List<ResultsItem?>?) : RecyclerView.Adapter<ExploreViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRowExploreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exploreData = exploreDataList?.get(position)
        with(holder){
            this.binding.exploreName.text = exploreData?.name
            binding.exploreName.movementMethod = ScrollingMovementMethod()
            this.binding.exploreReviewCount.text = "${exploreData?.userRatingsTotal} Reviews"
            this.binding.exploreRating.rating = exploreData?.rating.toString().toFloat()
            this.binding.exploreImage.load(photoUrl(exploreData?.photos?.get(0)?.photoReference))

            this.itemView.setOnClickListener{
                val placeId: String? = exploreData?.placeId
                val activity = this.itemView.context as AppCompatActivity
                val detailsFragment = DetailsFragment()
                detailsFragment.arguments = Bundle().apply {
                    putString(DetailsFragment.ARG_PLACE_ID, placeId)
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, detailsFragment).addToBackStack(null).commit()
                activity.supportFragmentManager.beginTransaction().remove(HomeFragment()).commit()
                activity.supportFragmentManager.beginTransaction().remove(CategoryFragment()).commit()
                activity.supportFragmentManager.beginTransaction().remove(FavoriteFragment()).commit()
                activity.supportFragmentManager.beginTransaction().remove(ExploreFragment()).commit()
            }
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