package com.example.galonapps.ui.pelanggan.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.App
import com.example.galonapps.model.Desa
import com.example.galonapps.model.Galon
import com.example.galonapps.model.ResponseData
import com.example.galonapps.network.ApiConfig
import com.example.galonapps.network.service.AuthService
import com.example.galonapps.network.service.DesaService
import com.example.galonapps.network.service.GalonService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var galonDataList = MutableLiveData<List<Galon>>()
    val getGalonDataList: LiveData<List<Galon>>
        get() = galonDataList
    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean>
        get() = _isLogout

    fun getGalon() {
        val retroInstance = ApiConfig.getRetroInstance().create(GalonService::class.java)
        val call = retroInstance.galon()
        call.enqueue(object : Callback<ResponseData<List<Galon>>> {
            override fun onResponse(
                call: Call<ResponseData<List<Galon>>>,
                response: Response<ResponseData<List<Galon>>>
            ) {
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        galonDataList.value = response.body()?.data?.filter { it.stok != 0 }
                    } else {
                        galonDataList.value = null
                    }
                else {
                    galonDataList.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<List<Galon>>>, t: Throwable) {
                galonDataList.value = null
            }

        })
    }

    fun logout() {
        val retroInstance = ApiConfig.getRetroInstance().create(AuthService::class.java)
        val call = retroInstance.logout()
        call.enqueue(object : Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                if (response.isSuccessful)
                    _isLogout.value = response.body()?.status == true
                else {
                    _isLogout.value = false
                }
            }

            override fun onFailure(call: Call<ResponseData<Any>>, t: Throwable) {
                _isLogout.value = false
            }

        })
    }
}
