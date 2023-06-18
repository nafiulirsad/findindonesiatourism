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
import com.fitdev.findindonesiatourism.dataclass.PulauResponseItem
import com.fitdev.findindonesiatourism.dataclass.RegionData
import com.fitdev.findindonesiatourism.remote.api.gmaps.GoogleMapsConfig
import com.fitdev.findindonesiatourism.remote.api.wisata.ApiConfig
import com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch.NearbySearchResponse
import com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.TextSearchResponse
import com.fitdev.findindonesiatourism.ui.adapter.NearbyViewAdapter
import com.fitdev.findindonesiatourism.ui.adapter.PopularViewAdapter
import com.fitdev.findindonesiatourism.ui.adapter.RegionViewAdapter
import com.fitdev.myapplication.BuildConfig
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val _byRegionData = MutableLiveData<MutableList<RegionData>>()
    private val byRegionData: LiveData<MutableList<RegionData>> = _byRegionData
    private val byRegionList: MutableList<RegionData> = mutableListOf()

    private val _byPopularData = MutableLiveData<List<com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem?>?>()
    private val byPopularData: LiveData<List<com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem?>?> = _byPopularData

    private val _byNearbyData = MutableLiveData<List<com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch.ResultsItem?>?>()
    private val byNearbyData: LiveData<List<com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch.ResultsItem?>?> = _byNearbyData

    private lateinit var myDialog: Dialog
    private lateinit var myLocation: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        myDialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            myLocation = it.getString(ARG_MY_LOCATION).toString()
        }

        byRegionList.clear()
        _byRegionData.value = byRegionList

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.home)
        binding.homeRvRegion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeRvPopular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeRvNearby.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        getWisataByRegion()
        getWisataByPopular()
        getWisataByNearby()
        observeByRegionData()
        observeByPopularData()
        observeByNearbyData()
    }

    private fun getWisataByRegion(){
        showLoading(true)
        val getAllPulau = ApiConfig.getApiService().getAllPulau()
        getAllPulau.enqueue(object: Callback<List<PulauResponseItem>> {
            override fun onResponse(call: Call<List<PulauResponseItem>>, response: Response<List<PulauResponseItem>>) {
                if(response.isSuccessful){
                    val res = response.body()
                    Log.d("Response Berhasil List Pulau", res.toString())
                    if (res.isNullOrEmpty()){
                        showLoading(false)
                        Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        res.forEach { pulau ->
                            val query = "Tourist Attractions in " + pulau.keyword + " Island, Indonesia"
                            val getPlace = GoogleMapsConfig.getGoogleMapsService().getSearchPlaceByQuery(BuildConfig.API_KEY, query, "true", "20000")
                            getPlace.enqueue(object : Callback<TextSearchResponse> {
                                override fun onResponse(call: Call<TextSearchResponse>, response: Response<TextSearchResponse>)
                                {
                                    showLoading(false)
                                    Log.d("Response (Pulau)", response.body().toString())
                                    if(response.isSuccessful){
                                        val placeRes: List<com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem?>? = response.body()?.results
                                        placeRes?.let {
                                            val wisataByRegion = RegionData(pulau.namaFotoRegion, pulau.keyword, it.size)
                                            byRegionList.add(wisataByRegion)
                                            _byRegionData.value = byRegionList
                                        }
                                    }
                                    else{
                                        Log.d("Response Gagal (Region)", response.toString())
                                        Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<TextSearchResponse>, t: Throwable) {
                                    showLoading(false)
                                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                }
                else{
                    showLoading(false)
                    Log.d("Response Gagal (Region)", response.toString())
                    Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PulauResponseItem>>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getWisataByPopular(){
        showLoading(true)
        val query = "Most Popular Tourist Attractions in Indonesia"
        val getPlace = GoogleMapsConfig.getGoogleMapsService().getSearchPlaceByQuery(BuildConfig.API_KEY, query, "true", "20000")
        getPlace.enqueue(object: Callback<TextSearchResponse> {
            override fun onResponse(call: Call<TextSearchResponse>, response: Response<TextSearchResponse>)
            {
                showLoading(false)
                if (response.isSuccessful){
                    val placeRes: List<com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.ResultsItem?>? = response.body()?.results
                    _byPopularData.value = placeRes
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

    private fun getWisataByNearby(){
        showLoading(true)
        Log.d("My Location", myLocation)
        val getPlace = GoogleMapsConfig.getGoogleMapsService().getNearbyPlaceByCoordinate(BuildConfig.API_KEY, myLocation, "30000", "tourist_attraction", "true")
        getPlace.enqueue(object: Callback<NearbySearchResponse> {
            override fun onResponse(call: Call<NearbySearchResponse>, response: Response<NearbySearchResponse>)
            {
                showLoading(false)
                if (response.isSuccessful){
                    val placeRes: List<com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch.ResultsItem?>? = response.body()?.results
                    _byNearbyData.value = placeRes
                }
                else{
                    showLoading(false)
                    Log.d("Response (Nearby)", response.body().toString())
                    Toast.makeText(requireActivity(), "Fetching nearby failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable)
            {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure Nearby: ${t.message}")
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun observeByRegionData(){
        byRegionData.observe(viewLifecycleOwner){regionData ->
            val adapter = RegionViewAdapter(regionData)
            binding.homeRvRegion.adapter = adapter
        }
    }

    private fun observeByPopularData(){
        byPopularData.observe(viewLifecycleOwner){popularData ->
            val adapter = PopularViewAdapter(popularData)
            binding.homeRvPopular.adapter = adapter
        }
    }

    private fun observeByNearbyData(){
        byNearbyData.observe(viewLifecycleOwner){nearbyData ->
            val adapter = NearbyViewAdapter(nearbyData)
            binding.homeRvNearby.adapter = adapter
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

    companion object {
        const val ARG_MY_LOCATION = "my_location"
    }
}