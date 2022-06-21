package com.example.galonapps.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.galonapps.config.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) : AppPreferencesHelper {
    private val loginPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.LOGIN_PREFS, Context.MODE_PRIVATE)
    private val customerPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.CUSTOMER_PREFS, Context.MODE_PRIVATE)
    private val cartPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.CART_PREFERENCES, Context.MODE_PRIVATE)

    override var name: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_NAME, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_NAME, value).apply()

    override var email: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_EMAIL, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_EMAIL, value).apply()

    override var role: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_ROLE, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_ROLE, value).apply()


    override var userId: Int?
        get() = loginPreferences.getInt(Constant.USER_ID,-1)
        set(value) = loginPreferences.edit().putInt(Constant.USER_ID, value!!).apply()


    override var shopList: String?
        get() = customerPreferences.getString(Constant.SHOP_LIST, null)
        set(value) = customerPreferences.edit().putString(Constant.SHOP_LIST, value).apply()

    override var cart: String?
        get() = cartPreferences.getString(Constant.CART, null)
        set(value) = cartPreferences.edit().putString(Constant.CART, value).apply()

    override var cartShop: String?
        get() = cartPreferences.getString(Constant.CART_SHOP, null)
        set(value) = cartPreferences.edit().putString(Constant.CART_SHOP, value).apply()

    override var cartDeliveryPref: String?
        get() = cartPreferences.getString(Constant.CART_DELIVERY, null)
        set(value) = cartPreferences.edit().putString(Constant.CART_DELIVERY, value).apply()

    override var cartShopInfo: String?
        get() = cartPreferences.getString(Constant.CART_SHOP_INFO, null)
        set(value) = cartPreferences.edit().putString(Constant.CART_SHOP_INFO, value).apply()

    override var cartDeliveryLocation: String?
        get() = cartPreferences.getString(Constant.CART_DELIVERY_LOCATION, null)
        set(value) = cartPreferences.edit().putString(Constant.CART_DELIVERY_LOCATION, value).apply()


    override fun clearPreferences() {
        loginPreferences.edit().clear().apply()
        customerPreferences.edit().clear().apply()
        cartPreferences.edit().clear().apply()
    }

    override fun clearCartPreferences(){
        cartPreferences.edit().clear().apply()
    }

//    fun getUser(): UserModel? {
//        return UserModel(userId, email, mobile, name, oauthId, role)
//    }
//
//    fun getCart(): List<MenuItemModel>? {
//        val listType = object : TypeToken<List<MenuItemModel?>?>() {}.type
//        return Gson().fromJson(cart, listType)
//    }
//
//    fun getShopList(): List<ShopConfigurationModel>? {
//        val listType = object : TypeToken<List<ShopConfigurationModel?>?>() {}.type
//        return Gson().fromJson(shopList, listType)
//    }
//
//    fun getCartShop(): ShopConfigurationModel? {
//        return Gson().fromJson(cartShop, ShopConfigurationModel::class.java)
//    }

}
