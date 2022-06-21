package com.example.galonapps.storage

interface AppPreferencesHelper {

    val name: String?
    val email: String?
    val role: String?
    val userId: Int?
    val cart: String?
    val shopList: String?
    val cartShop: String?
    val cartDeliveryPref: String?
    val cartShopInfo: String?
    val cartDeliveryLocation: String?

    fun clearPreferences()

    fun clearCartPreferences()
}
