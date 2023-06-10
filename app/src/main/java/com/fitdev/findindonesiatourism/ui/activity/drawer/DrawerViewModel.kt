package com.fitdev.findindonesiatourism.ui.activity.drawer

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.fitdev.findindonesiatourism.remote.UserInstance
import com.fitdev.findindonesiatourism.remote.api.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrawerViewModel (
    private val preferences: UserInstance,
) : ViewModel() {
    fun getToken(): LiveData<String> {
        return preferences.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
        }
    }

//    fun getData(token: String){
//        val client = ApiConfig.getApiService().getStories(("Bearer $token"))
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
//                if (response.isSuccessful) {
//                    _listStory.value = response.body()?.listStory as List<ListStory>?
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//
//        })
//    }

    class Factory(private val preferences: UserInstance) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(DrawerViewModel::class.java) -> {
                    DrawerViewModel(preferences) as T
                }

                else -> throw IllegalArgumentException("Unknown Viewmodel Class: " + modelClass.name)
            }
        }
    }
}