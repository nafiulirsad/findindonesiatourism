package com.fitdev.findindonesiatourism.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitdev.findindonesiatourism.remote.api.categories.TourismCategoriesConfig
import com.fitdev.findindonesiatourism.remote.response.categories.CategoriesDataItem
import com.fitdev.findindonesiatourism.remote.response.categories.CategoriesResponse
import com.fitdev.findindonesiatourism.ui.adapter.CategoryViewAdapter
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.FragmentCategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val _categoryData = MutableLiveData<List<CategoriesDataItem?>?>()
    private val categoryData: LiveData<List<CategoriesDataItem?>?> = _categoryData

    private lateinit var myDialog: Dialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        myDialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.category)

        binding.categoriesRv.layoutManager = LinearLayoutManager(requireContext())

        getCategoryData()
        observeCategoryData()
    }

    private fun getCategoryData(){
        showLoading(true)
        val getCategory = TourismCategoriesConfig.getCategoriesService().getCategories()
        getCategory.enqueue(object: Callback<CategoriesResponse>{
            override fun onResponse(call: Call<CategoriesResponse>, response: Response<CategoriesResponse>)
            {
                showLoading(false)
                Log.d("Response (Category)", response.body().toString())
                if(response.isSuccessful){
                    val categoryRes: List<CategoriesDataItem?>? = response.body()?.categoriesData
                    categoryRes.let {
                        _categoryData.value = it
                    }
                }
                else{
                    Log.d("Response Gagal (Category)", response.toString())
                    Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable)
            {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun observeCategoryData(){
        categoryData.observe(viewLifecycleOwner){ category ->
            val adapter = CategoryViewAdapter(category)
            binding.categoriesRv.adapter = adapter
        }
    }

    @SuppressLint("InflateParams")
    private fun showLoading(con: Boolean){
        val dialogBinding = layoutInflater.inflate(R.layout.loading_dialog, null)
        myDialog.setContentView(dialogBinding)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if(con){
            myDialog.setCancelable(false)
            myDialog.show()
        }
        else{
            myDialog.setCancelable(true)
            myDialog.dismiss()
        }
    }
}