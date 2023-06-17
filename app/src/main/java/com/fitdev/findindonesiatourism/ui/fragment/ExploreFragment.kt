package com.fitdev.findindonesiatourism.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitdev.findindonesiatourism.remote.api.gmaps.GoogleMapsConfig
import com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem
import com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.TextSearchResponse
import com.fitdev.findindonesiatourism.ui.adapter.ExploreViewAdapter
import com.fitdev.myapplication.BuildConfig
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.FragmentExploreBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val _byExploreData = MutableLiveData<List<ResultsItem?>?>()
    private val byExploreData: LiveData<List<ResultsItem?>?> = _byExploreData

    private lateinit var myDialog: Dialog
    private var searchParams: String? = null
    private var searchType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        myDialog = Dialog(requireActivity())
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.explore)
        arguments?.let {
            searchParams = it.getString(ARG_SEARCH_PARAMS)
            searchType = it.getString(ARG_SEARCH_TYPE)
        }

        binding.exploreRv.layoutManager = LinearLayoutManager(requireContext())

        autoHideKeyboardFragment()
        binding.exploreInputSearch.setOnEditorActionListener { _, actionId, _ ->
            Log.d("Action ID", actionId.toString())
            if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO ||actionId == EditorInfo.IME_MASK_ACTION || actionId == EditorInfo.IME_ACTION_DONE){
                hideKeyboardFragment()
                    val inputSearchText = binding.exploreInputSearch.text
                    Log.d("Input Search", inputSearchText.toString())
                    getDataBySearch(inputSearchText.toString())
                    //searchLog(inputSearchText.toString())
                true
            } else {
                false
            }
        }
        if(searchParams.isNullOrEmpty()){
            binding.exploreShowing.visibility = View.GONE
        }
        else{
            var query = ""
            if (searchType == "Region"){
                query = "Tourist Attractions in $searchParams Island, Indonesia"
            }
            else if (searchType == "Category"){
                query = "The Most Beautiful $searchParams Tourist Attractions in Indonesia"
            }
            getDataBySearch(query)
        }
        observeExploreData()
    }

    private fun getDataBySearch(search: String){
        showLoading(true)
        val getPlace = GoogleMapsConfig.getGoogleMapsService().getSearchPlaceByQuery(BuildConfig.API_KEY, search, "true", "20000")
        getPlace.enqueue(object: Callback<TextSearchResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<TextSearchResponse>, response: Response<TextSearchResponse>)
            {
                showLoading(false)
                if (response.isSuccessful){
                    val placeRes: List<ResultsItem?>? = response.body()?.results
                    _byExploreData.value = placeRes
                    binding.exploreShowing.text = "Showing ${placeRes?.size} tourist destinations from $searchParams"
                    Log.d("Response (Popular)", placeRes.toString())
                }
                else{
                    showLoading(false)
                    Log.d("Response Gagal Popular", response.toString())
                    Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TextSearchResponse>, t: Throwable)
            {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure Pupular: ${t.message}")
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun observeExploreData(){
        byExploreData.observe(viewLifecycleOwner){exploreData ->
            val adapter = ExploreViewAdapter(exploreData)
            binding.exploreRv.adapter = adapter
            binding.exploreShowing.text = "Showing ${exploreData?.size} tourist destinations in $searchParams"
        }
    }

    private fun autoHideKeyboardFragment(){
        binding.exploreLayout.setOnClickListener{
            hideKeyboardFragment()
        }
    }

    private fun hideKeyboardFragment(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
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

    companion object{
        const val ARG_SEARCH_PARAMS = "region_name"
        const val ARG_SEARCH_TYPE = "search_type"
    }
}