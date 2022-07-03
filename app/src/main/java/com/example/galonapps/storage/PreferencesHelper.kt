package com.example.galonapps.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.galonapps.config.Constant
import com.example.galonapps.model.CartItem
import com.example.galonapps.model.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) : AppPreferencesHelper {
    private val loginPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.LOGIN_PREFS, Context.MODE_PRIVATE)
    private val customerPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.CUSTOMER_PREFS, Context.MODE_PRIVATE)
    private val cartPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.CART_PREFERENCES, Context.MODE_PRIVATE)

    override var isLoggedIn: Boolean
        get() = loginPreferences.getBoolean(Constant.IS_LOGGED_IN, false)
        set(value) = loginPreferences.edit().putBoolean(Constant.IS_LOGGED_IN, value).apply()
    override var id: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_ID, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_ID, value).apply()
    override var nama: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_NAMA, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_NAMA, value).apply()
    override var tempatLahir: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_TEMPAT_LAHIR, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_TEMPAT_LAHIR, value).apply()
    override var tanggalLahir: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_TANGGAL_LAHIR, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_TANGGAL_LAHIR, value).apply()
    override var jenisKelamin: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_JENIS_KELAMIN, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_JENIS_KELAMIN, value).apply()
    override var alamat: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_ALAMAT, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_ALAMAT, value).apply()
    override var member: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_MEMBER, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_MEMBER, value).apply()
    override var desa: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_DESA, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_DESA, value).apply()
    override var langUser: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_LANG_USER, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_LANG_USER, value).apply()
    override var longUser: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_LONG_USER, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_LONG_USER, value).apply()
    override var role: String?
        get() = customerPreferences.getString(Constant.CUSTOMER_ROLE, null)
        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_ROLE, value).apply()
//    override var langPelanggan: String?
//        get() = customerPreferences.getString(Constant.CUSTOMER_LANG_PELANGGAN, null)
//        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_LANG_PELANGGAN, value).apply()
//    override var longPelanggan: String?
//        get() = customerPreferences.getString(Constant.CUSTOMER_LONG_PELANGGAN, null)
//        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_LONG_PELANGGAN, value).apply()
//    override var idPelanggan: String?
//        get() = customerPreferences.getString(Constant.CUSTOMER_ID_PELANGGAN, null)
//        set(value) = customerPreferences.edit().putString(Constant.CUSTOMER_ID_PELANGGAN, value).apply()

    override var cart: String?
        get() = cartPreferences.getString(Constant.CART, null)
        set(value) = cartPreferences.edit().putString(Constant.CART, value).apply()


    override fun clearPreferences() {
        loginPreferences.edit().clear().apply()
        customerPreferences.edit().clear().apply()
        cartPreferences.edit().clear().apply()
    }

    override fun clearCartPreferences() {
        cartPreferences.edit().clear().apply()
    }

    fun saveUser(user: User) {
        id = user.id
        nama = user.nama
        tempatLahir = user.tempatLahir
        tanggalLahir = user.tanggalLahir
        jenisKelamin = user.jenisKelamin
        alamat = user.alamat
        member = user.member.toString() ?: "0"
        desa = user.desa ?: ""
        langUser = (user.lang.toString() ?: "") as String?
        longUser = (user.long.toString() ?: "") as String?
        role = user.role ?: ""
        isLoggedIn = true
    }

    //    fun getUser(): Pelanggan? {
//        return
//    }
    fun saveCart(galonItems: List<CartItem>) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val cartString = gson.toJson(galonItems)
        cart = cartString
    }

    //
    fun getCart(): List<CartItem>? {
        val listType = object : TypeToken<List<CartItem?>?>() {}.type
        return Gson().fromJson(cart, listType)
    }

}
