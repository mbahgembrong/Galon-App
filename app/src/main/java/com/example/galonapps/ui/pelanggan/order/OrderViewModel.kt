package com.example.galonapps.ui.pelanggan.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.App
import com.example.galonapps.App.Companion.prefs
import com.example.galonapps.model.Transaksi
import com.example.galonapps.model.ResponseData
import com.example.galonapps.model.TransaksiRequest

import com.example.galonapps.network.ApiConfig
import com.example.galonapps.network.service.TransaksiService
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class OrderViewModel : ViewModel() {

    private var orderDataList = MutableLiveData<List<Transaksi>>()
    val getOrderDataList: LiveData<List<Transaksi>>
        get() = orderDataList
    private val _isUpdateTransaksi = MutableLiveData<Boolean>()
    val isUpdateTransaksi: LiveData<Boolean>
        get() = _isUpdateTransaksi

    fun getOrder(statusSelected: String? = null) {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.indexPelanggan(prefs?.idPelanggan!!)
        call.enqueue(object : Callback<ResponseData<List<Transaksi>>> {
            override fun onResponse(
                call: Call<ResponseData<List<Transaksi>>>,
                response: Response<ResponseData<List<Transaksi>>>
            ) {

                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        Timber.d("message 1")
                        if (statusSelected == null) {
                            orderDataList.value = response.body()?.data
                        } else {
                            orderDataList.value =
                                response.body()?.data?.filter { it.status == statusToInt(statusSelected) }
                        }
                    } else {
                        Timber.d("message 2")
                        orderDataList.value = null
                    }
                else {
                    Timber.d("message 3")
                    orderDataList.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<List<Transaksi>>>, t: Throwable) {
                Timber.d("message 4")
                Timber.d("message : ${t.message}")
                orderDataList.value = null
            }

        })
    }

    fun cancelOrder(id: String) {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.updatePelanggan(
            TransaksiRequest(
                id,
                prefs?.idPelanggan!!,
                null,
                null,
                null,
                5,
            )
        )
        call.enqueue(object : Callback<ResponseData<Transaksi?>?> {
            override fun onResponse(
                call: Call<ResponseData<Transaksi?>?>,
                response: Response<ResponseData<Transaksi?>?>
            ) {
                if (response.isSuccessful)
                    _isUpdateTransaksi.value = response.body()?.status == true
                else {
                    _isUpdateTransaksi.value = false
                }
            }

            override fun onFailure(call: Call<ResponseData<Transaksi?>?>, t: Throwable) {
                _isUpdateTransaksi.value = false
            }

        })
    }

    private fun statusToInt(status: String): Int {
        return when (status) {
            "belum dikonfirmasi" -> 0
            "belum dibayar" -> 1
            "pembayaran gagal" -> 2
            "belum dikirim" -> 3
            "selesai" -> 4
            "dibatalkan" -> 5
            else -> 0
        }
    }

    fun bayarOrder(id: String): Boolean {
//        val transaksi = App.TransaksiDataList.find { it.id == id }
//        transaksi?.status = 0
        return true
    }
}
