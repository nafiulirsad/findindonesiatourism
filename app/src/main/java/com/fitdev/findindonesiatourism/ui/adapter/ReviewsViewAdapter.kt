package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails.ReviewsItem
import com.fitdev.myapplication.databinding.ItemReviewsBinding

class ReviewsViewAdapter(private val reviewsList : List<ReviewsItem?>?) : RecyclerView.Adapter<ReviewsViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemReviewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviewsData = reviewsList?.get(position)
        with(holder){
            this.binding.reviewValue.text = "${reviewsData?.text} (${reviewsData?.authorName})"
        }
    }

    override fun getItemCount(): Int {
        return reviewsList?.size ?: 0
    }
}