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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitdev.findindonesiatourism.dataclass.PulauResponseItem
import com.fitdev.findindonesiatourism.dataclass.RegionData
import com.fitdev.findindonesiatourism.dataclass.wisata.ResultsItem
import com.fitdev.findindonesiatourism.dataclass.wisata.WisataResponse
import com.fitdev.findindonesiatourism.remote.api.wisata.ApiConfig
import com.fitdev.findindonesiatourism.ui.adapter.RegionViewAdapter
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

    private lateinit var myDialog: Dialog

    init {
        byRegionList.clear()
        _byRegionData.value = byRegionList
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        myDialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeRvRegion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        getWisataByRegion()
        observeByRegionData()
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
                            val query = "Wisata di Pulau " + pulau.keyword
                            val getAllWisata = ApiConfig.getApiService().getAllWisata(query)
                            getAllWisata.enqueue(object : Callback<WisataResponse> {
                                override fun onResponse(call: Call<WisataResponse>, response: Response<WisataResponse>)
                                {
                                    showLoading(false)
                                    if(response.isSuccessful){
                                        val wisataRes = response.body()
                                        val wisataData: List<ResultsItem>? = wisataRes?.results
                                        if(wisataData.isNullOrEmpty()){
                                            showLoading(false)
                                            Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                                        }
                                        else{
                                            val wisataByRegion = RegionData(pulau.namaFotoRegion, pulau.keyword, wisataData.size)
                                            byRegionList.add(wisataByRegion)
                                            _byRegionData.value = byRegionList
                                            Log.d("Update Data", byRegionList.toString())
                                        }
                                    }
                                    else{
                                        Log.d("Response Gagal List Wisata", response.toString())
                                        Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<WisataResponse>, t: Throwable) {
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
                    Log.d("Response Gagal List Pulau", response.toString())
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

    private fun observeByRegionData(){
        byRegionData.observe(viewLifecycleOwner){regionData ->
            val adapter = RegionViewAdapter(regionData)
            binding.homeRvRegion.adapter = adapter
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