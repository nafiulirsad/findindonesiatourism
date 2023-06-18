package com.fitdev.findindonesiatourism.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails.PhotosItem
import com.fitdev.myapplication.databinding.ItemMorePhotosBinding

class MorePhotosViewAdapter(private val morePhotosList : List<PhotosItem?>?) : RecyclerView.Adapter<MorePhotosViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMorePhotosBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMorePhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val morePhotosData = morePhotosList?.get(position)
        with(holder){
            this.binding.morePhotosImage.load(photoUrl(morePhotosData?.photoReference))
        }
    }

    override fun getItemCount(): Int {
        return morePhotosList?.size ?: 0
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