package com.example.galonapps.ui.pelanggan.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.App
import com.example.galonapps.model.Galon
import com.example.galonapps.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var galonDataList= MutableLiveData<List<Galon>>()
    val getGalonDataList :LiveData<List<Galon>>
        get() = galonDataList

    fun getGalon() {
        galonDataList.value = App.GalonDataList
    }
}
