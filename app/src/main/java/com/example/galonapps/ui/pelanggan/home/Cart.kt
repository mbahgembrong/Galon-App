package com.example.galonapps.ui.pelanggan.home

import android.util.Log
import com.example.galonapps.model.CartItem
import com.example.galonapps.model.Galon
import com.example.galonapps.prefs

class Cart {
    companion object {
        fun addItem(cartItem: CartItem) {
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
        }
        fun showItem(galon: Galon): CartItem? {
            var cart: MutableList<CartItem> = if (prefs.getCart().isNullOrEmpty())
                mutableListOf()
            else
                prefs.getCart() as MutableList<CartItem>
            return cart.singleOrNull { it.galon.id == galon.id }
        }

        fun removeItem(cartItem: CartItem) {
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
        }


        fun getShoppingCartSize(): Int {
            var cartSize = 0
            prefs.getCart()?.forEach {
                cartSize += it.quantity;
            }
            return cartSize
        }
        fun  getShoppingTotal():Int{
            var cartGrandTotal = 0
            prefs.getCart()?.forEach {
                cartGrandTotal += it.total;
            }
            return cartGrandTotal
        }
    }

}
