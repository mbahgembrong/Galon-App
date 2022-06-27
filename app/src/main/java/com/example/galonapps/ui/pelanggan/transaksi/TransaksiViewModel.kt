package com.example.galonapps.ui.pelanggan.transaksi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.App
import com.example.galonapps.model.CartItem
import com.example.galonapps.model.DetailTransaksi
import com.example.galonapps.model.Transaksi
import com.example.galonapps.prefs
import java.util.UUID

class TransaksiViewModel:ViewModel() {
    private var cartDataList = MutableLiveData<List<CartItem>>()
    val getCartDataList:LiveData<List<CartItem>>
        get() = cartDataList
    private var grandTotal = MutableLiveData<Int>()
    val getGrandTotal:LiveData<Int>
        get() = grandTotal
    fun getCart(){
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
        getGrandTotal()
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
        getGrandTotal()
    }
    fun getGrandTotal(){
        var total = 0
        val cart = prefs.getCart() as MutableList<CartItem>
        for (item in cart){
            total += item.total
        }
        grandTotal.value = total
    }

    fun addTransaksi() {
        val detailTransaksi= mutableListOf<DetailTransaksi>()
        cartDataList.value!!.forEach {
            detailTransaksi.add(DetailTransaksi(UUID.randomUUID().toString(),null,it.galon.id, it.quantity, it.total,it.galon))
        }
        App.TransaksiDataList.add(Transaksi(UUID.randomUUID().toString(), null,null, null,null, grandTotal.value!!,1, null, detailTransaksi,null))
        prefs.clearCartPreferences()
    }

    fun bayar() {
        val detailTransaksi= mutableListOf<DetailTransaksi>()
        cartDataList.value!!.forEach {
            detailTransaksi.add(DetailTransaksi(UUID.randomUUID().toString(),null,it.galon.id, it.quantity, it.total, it.galon))
        }
        App.TransaksiDataList.add(Transaksi(UUID.randomUUID().toString(), null,null, null,null, grandTotal.value!!,0, null, detailTransaksi,null))
        prefs.clearCartPreferences()
    }
}
