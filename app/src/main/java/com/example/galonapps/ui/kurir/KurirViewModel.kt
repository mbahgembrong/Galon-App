package com.example.galonapps.ui.kurir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.model.Transaksi

class KurirViewModel : ViewModel() {
    private val _orderKurirList = MutableLiveData<List<Transaksi>>()
    val getOrderKurirList: LiveData<List<Transaksi>>
        get() = _orderKurirList

    fun getOrderKurir() {

    }

}
