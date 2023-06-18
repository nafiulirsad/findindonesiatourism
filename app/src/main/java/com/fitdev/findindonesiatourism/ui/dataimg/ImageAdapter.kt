package com.fitdev.findindonesiatourism.ui.dataimg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ListSlideBinding

class ImageAdapter(private val items: List<ImageData>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){
    inner class ImageViewHolder(itemView: ListSlideBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        fun bind(data: ImageData) {
            with(binding) {
                imgGambar.load(data.imgUrl){
                    placeholder(R.drawable.ic_loading)
                    transformations(RoundedCornersTransformation(24F))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ImageViewHolder
    {
        val view = ListSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}