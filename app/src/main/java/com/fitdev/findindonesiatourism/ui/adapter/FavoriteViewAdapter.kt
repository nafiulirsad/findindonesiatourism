package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.database.Favorite
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.DetailsFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ItemRowFavoriteBinding

class FavoriteViewAdapter(private val favoriteDataList : List<Favorite?>?) : RecyclerView.Adapter<FavoriteViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRowFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteData = favoriteDataList?.get(position)
        with(holder){
            this.binding.favoriteName.text = favoriteData?.placeName
            binding.favoriteName.movementMethod = ScrollingMovementMethod()
            this.binding.favoriteReviewCount.text = "${favoriteData?.placeRatingsTotal} Reviews"
            this.binding.favoriteRating.rating = favoriteData?.placeRating.toString().toFloat()
            this.binding.favoriteImage.load(photoUrl(favoriteData?.placePhotos))

            this.itemView.setOnClickListener{
                val placeId: String? = favoriteData?.placeId
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
        return favoriteDataList?.size ?: 0
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