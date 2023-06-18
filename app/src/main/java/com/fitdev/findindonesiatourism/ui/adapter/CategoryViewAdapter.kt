package com.fitdev.findindonesiatourism.ui.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fitdev.findindonesiatourism.remote.response.categories.CategoriesDataItem
import com.fitdev.findindonesiatourism.ui.fragment.CategoryFragment
import com.fitdev.findindonesiatourism.ui.fragment.DetailsFragment
import com.fitdev.findindonesiatourism.ui.fragment.ExploreFragment
import com.fitdev.findindonesiatourism.ui.fragment.FavoriteFragment
import com.fitdev.findindonesiatourism.ui.fragment.HomeFragment
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.ItemRowCategoryBinding

class CategoryViewAdapter(private val categoryDataList : List<CategoriesDataItem?>?) : RecyclerView.Adapter<CategoryViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRowCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryData = categoryDataList?.get(position)
        with(holder){
            this.binding.categoriesName.text = categoryData?.categoryName
            this.binding.categoriesImage.load(photoUrl(categoryData?.categoryImage))

            this.itemView.setOnClickListener{
                val categoryName: String? = categoryData?.categoryName
                val activity = this.itemView.context as AppCompatActivity
                val exploreFragment = ExploreFragment()
                exploreFragment.arguments = Bundle().apply {
                    putString(ExploreFragment.ARG_SEARCH_PARAMS, categoryName)
                    putString(ExploreFragment.ARG_SEARCH_TYPE, "Category")
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
        return categoryDataList?.size ?: 0
    }

    private fun photoUrl(categoryImage: String?): String{
        return if(categoryImage.isNullOrEmpty()){
            "https://aplikasijpm.online/fitproject/default/defaultimage.png"
        } else{
            "https://aplikasijpm.online/fitproject/categories/$categoryImage"
        }
    }
}