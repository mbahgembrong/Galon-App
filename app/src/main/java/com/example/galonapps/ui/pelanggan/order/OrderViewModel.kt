package com.example.galonapps.ui.pelanggan.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.App
import com.example.galonapps.model.Transaksi

class OrderViewModel : ViewModel() {

  private var orderDataList = MutableLiveData<List<Transaksi>>()
    val getOrderDataList: LiveData<List<Transaksi>>
        get() = orderDataList
    fun getOrder(){
        orderDataList.value = App.TransaksiDataList
    }
    fun cancelOrder(id: String):Boolean {
        val transaksi = App.TransaksiDataList.find { it.id == id }
        transaksi?.status = 5
        return true
    }

    fun bayarOrder(id: String): Boolean {
        val transaksi = App.TransaksiDataList.find { it.id == id }
        transaksi?.status = 0
        return true
    }
}
