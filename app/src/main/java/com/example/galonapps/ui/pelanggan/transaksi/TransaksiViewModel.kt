package com.example.galonapps.ui.pelanggan.transaksi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.App
import com.example.galonapps.model.*
import com.example.galonapps.network.ApiConfig
import com.example.galonapps.network.service.AuthService
import com.example.galonapps.network.service.DesaService
import com.example.galonapps.network.service.TransaksiService
import com.example.galonapps.prefs
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.UUID

class TransaksiViewModel : ViewModel() {
    private var cartDataList = MutableLiveData<List<CartItem>>()
    val getCartDataList: LiveData<List<CartItem>>
        get() = cartDataList
    private var grandTotal = MutableLiveData<Int>()
    val getHargaTotal: LiveData<Int>
        get() = grandTotal
    private val _listDesa = MutableLiveData<List<Desa>>()
    val getListDesa: LiveData<List<Desa>>
        get() = _listDesa
    private val _isUpdated = MutableLiveData<Int>()
    val isUpdated: LiveData<Int>
        get() = _isUpdated
    private val _transaksi = MutableLiveData<Transaksi>()
    val getTransaksi: LiveData<Transaksi>
        get() = _transaksi
    private val _isUpdateTransaksi = MutableLiveData<Boolean>()
    val isUpdateTransaksi: LiveData<Boolean>
        get() = _isUpdateTransaksi

    fun getCart() {
        cartDataList.value = if (prefs.getCart().isNullOrEmpty())
            mutableListOf()
        else
            prefs.getCart() as MutableList<CartItem>
    }

    fun addQuantity(cartItem: CartItem) {
        var cart: MutableList<CartItem> = if (prefs.getCart().isNullOrEmpty())
            mutableListOf()
        else
            prefs.getCart() as MutableList<CartItem>
        val targetItem = cart.singleOrNull { it.galon.id == cartItem.galon.id }
        if (targetItem == null) {
            cartItem.quantity++
            cartItem.total = cartItem.quantity * cartItem.galon.hargaJual
            cart.add(cartItem)
        } else {
            targetItem.quantity++
            targetItem.total = targetItem.quantity * targetItem.galon.hargaJual
        }
        prefs.saveCart(cart)
        getCart()
        getHargaTotal()
    }

    fun subQuantity(cartItem: CartItem) {
        val cart = prefs.getCart() as MutableList<CartItem>
        val targetItem = cart.singleOrNull { it.galon.id == cartItem.galon.id }
        if (targetItem != null) {
            if (targetItem.quantity > 1) {
                targetItem.quantity--
                targetItem.total = targetItem.quantity * targetItem.galon.hargaJual
            } else {
                cart.remove(targetItem)
            }
        }
        prefs.saveCart(cart)
        getCart()
        getHargaTotal()
    }

    fun getHargaTotal() {
        var total = 0
        val cart = prefs.getCart() as MutableList<CartItem>
        for (item in cart) {
            total += item.total
        }
        grandTotal.value = total
    }

    fun addTransaksi(transaksiRequest: TransaksiRequest) {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.createPelanggan(transaksiRequest)
        call.enqueue(object : Callback<ResponseData<Transaksi>> {
            override fun onResponse(
                call: Call<ResponseData<Transaksi>>,
                response: Response<ResponseData<Transaksi>>
            ) {
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        prefs.clearCartPreferences()
                        _transaksi.value = response.body()?.data
                    } else {
                        _transaksi.value = null
                    }
                else {
                    _transaksi.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<Transaksi>>, t: Throwable) {
                Timber.e(t.message)
                _transaksi.value = null
            }

        })
    }

    fun bayar(transaksiRequest: TransaksiRequest) {
        val retroInstance = ApiConfig.getRetroInstance().create(TransaksiService::class.java)
        val call = retroInstance.updatePelanggan(
            TransaksiRequest(
                transaksiRequest.id,
                prefs.idPelanggan,
                null,
                null,
                transaksiRequest.fileImage,
                transaksiRequest.statusTransaksi ?: 0,
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
                Timber.e(t.message)
                _isUpdateTransaksi.value = false
            }

        })
    }

    fun updateAlamat(desa: String, alamat: String, point: Point) {
        val retroInstance = ApiConfig.getRetroInstance().create(AuthService::class.java)
        val call = retroInstance.update(
            prefs.idUser!!,
            prefs.nama!!,
            prefs.tempatLahir!!,
            prefs.tanggalLahir!!,
            prefs.jenisKelamin!!,
            alamat,
            desa,
            point.latitude(),
            point.longitude(),
            null,
            null
        )
        call.enqueue(object : Callback<ResponseData<List<User>>?> {
            override fun onResponse(
                call: Call<ResponseData<List<User>>?>,
                response: Response<ResponseData<List<User>>?>
            ) {
                Timber.d("onResponse: ${response.body()}")
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        _isUpdated.value = 200
                        response.body()?.data?.first().let {
                            prefs.alamat = it?.alamat
                            prefs.saveDesa(it?.desa!!)
                            prefs.langUser = it?.lang!!.toString()
                            prefs.longUser = it?.long!!.toString()
                        }
                        Timber.d("message 1: ${response.body()}")
                    } else {
                        _isUpdated.value = 205
                        Timber.d("message 2: ${response}")
                    }
                else {
                    Timber.d("message 3: ${response}")
                    Timber.d("message 3: ${call}")
                    _isUpdated.value = 500
                }
            }

            override fun onFailure(call: Call<ResponseData<List<User>>?>, t: Throwable) {
                Timber.d("message 4: ${t.message}")
                _isUpdated.value = 404
            }

        })
    }

    fun getDesa() {
        val retroInstance = ApiConfig.getRetroInstance().create(DesaService::class.java)
        val call = retroInstance.desa()
        call.enqueue(object : Callback<ResponseData<List<Desa>>> {
            override fun onResponse(
                call: Call<ResponseData<List<Desa>>>,
                response: Response<ResponseData<List<Desa>>>
            ) {
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        _listDesa.value = response.body()?.data
                    } else {
                        _listDesa.value = null
                    }
                else {
                    _listDesa.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<List<Desa>>>, t: Throwable) {
                _listDesa.value = null
            }

        })
    }
}
