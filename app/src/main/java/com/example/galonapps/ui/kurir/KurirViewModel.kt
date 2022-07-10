package com.example.galonapps.ui.kurir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.model.*
import com.example.galonapps.network.ApiConfig
import com.example.galonapps.network.service.AuthService
import com.example.galonapps.network.service.DesaService
import com.example.galonapps.network.service.TransaksiService
import com.example.galonapps.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class KurirViewModel : ViewModel() {
    private val _orderKurirList = MutableLiveData<List<Transaksi>>()
    val getOrderKurirList: LiveData<List<Transaksi>>
        get() = _orderKurirList
    private val _laporanKurirList = MutableLiveData<List<LaporanKurir>>()
    val getLaporanKurirList: LiveData<List<LaporanKurir>>
        get() = _laporanKurirList
    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean>
        get() = _isLogout
    private val _isComplete = MutableLiveData<Boolean>()
    val isComplete: LiveData<Boolean>
        get() = _isComplete

    fun getOrderKurir() {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.indexKurir(prefs.idPelanggan!!)
        call.enqueue(object : Callback<ResponseData<List<Transaksi>>> {
            override fun onResponse(
                call: Call<ResponseData<List<Transaksi>>>,
                response: Response<ResponseData<List<Transaksi>>>
            ) {
                Timber.d("onResponse: ${prefs.token}")
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        _orderKurirList.value = response.body()?.data?.filter {
                            it.status == 3
                        }
                    } else {
                        _orderKurirList.value = null
                    }
                else {
                    _orderKurirList.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<List<Transaksi>>>, t: Throwable) {
                Timber.d("onFailure: ${t.message}")
                _orderKurirList.value = null
            }

        })

    }

    fun getLaporanKurir() {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.laporanKurir(prefs.idPelanggan!!)
        call.enqueue(object : Callback<ResponseData<List<LaporanKurir>>> {
            override fun onResponse(
                call: Call<ResponseData<List<LaporanKurir>>>,
                response: Response<ResponseData<List<LaporanKurir>>>
            ) {
                Timber.d("onResponse: ${prefs.token}")
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        _laporanKurirList.value = response.body()?.data
                    } else {
                        _laporanKurirList.value = null
                    }
                else {
                    _laporanKurirList.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<List<LaporanKurir>>>, t: Throwable) {
                Timber.d("onFailure: ${t.message}")
                _laporanKurirList.value = null
            }

        })
    }

    fun complete(transaksiRequest: TransaksiRequest) {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.updateKurir(
            TransaksiRequest(
                transaksiRequest.id,
                transaksiRequest.idPelanggan,
                prefs.idPelanggan,
                null,
                null,
                transaksiRequest.statusTransaksi ?: 4,
            )
        )
        call.enqueue(object : Callback<ResponseData<Transaksi?>?> {
            override fun onResponse(
                call: Call<ResponseData<Transaksi?>?>,
                response: Response<ResponseData<Transaksi?>?>
            ) {
                if (response.isSuccessful)
                    _isComplete.value = response.body()?.status == true
                else {
                    _isComplete.value = false
                }
            }

            override fun onFailure(call: Call<ResponseData<Transaksi?>?>, t: Throwable) {
                _isComplete.value = false
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
