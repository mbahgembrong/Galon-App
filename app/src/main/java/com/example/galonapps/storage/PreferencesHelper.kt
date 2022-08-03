package com.example.galonapps.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.galonapps.config.Constant
import com.example.galonapps.model.CartItem
import com.example.galonapps.model.Desa
import com.example.galonapps.model.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) : AppPreferencesHelper {
    private val loginPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.LOGIN_PREFS, Context.MODE_PRIVATE)
    private val customerPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.USER_PREFS, Context.MODE_PRIVATE)
    private val cartPreferences: SharedPreferences =
        context.getSharedPreferences(Constant.CART_PREFERENCES, Context.MODE_PRIVATE)

    override var isLoggedIn: Boolean
        get() = loginPreferences.getBoolean(Constant.IS_LOGGED_IN, false)
        set(value) = loginPreferences.edit().putBoolean(Constant.IS_LOGGED_IN, value).apply()
    override var idUser: String?
        get() = customerPreferences.getString(Constant.USER_ID_USER, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_ID_USER, value).apply()
    override var idPelanggan: String?
        get() = customerPreferences.getString(Constant.USER_ID_PELANGGAN, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_ID_PELANGGAN, value).apply()
    override var nama: String?
        get() = customerPreferences.getString(Constant.USER_NAMA, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_NAMA, value).apply()
    override var tempatLahir: String?
        get() = customerPreferences.getString(Constant.USER_TEMPAT_LAHIR, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_TEMPAT_LAHIR, value).apply()
    override var tanggalLahir: String?
        get() = customerPreferences.getString(Constant.USER_TANGGAL_LAHIR, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_TANGGAL_LAHIR, value).apply()
    override var jenisKelamin: String?
        get() = customerPreferences.getString(Constant.USER_JENIS_KELAMIN, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_JENIS_KELAMIN, value).apply()
    override var alamat: String?
        get() = customerPreferences.getString(Constant.USER_ALAMAT, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_ALAMAT, value).apply()
    override var member: String?
        get() = customerPreferences.getString(Constant.USER_MEMBER, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_MEMBER, value).apply()
    override var desa: String?
        get() = customerPreferences.getString(Constant.USER_DESA, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_DESA, value).apply()
    override var langUser: String?
        get() = customerPreferences.getString(Constant.USER_LANG, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_LANG, value).apply()
    override var longUser: String?
        get() = customerPreferences.getString(Constant.USER_LONG, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_LONG, value).apply()
    override var role: String?
        get() = customerPreferences.getString(Constant.USER_ROLE, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_ROLE, value).apply()
    override var token: String?
        get() = customerPreferences.getString(Constant.USER_TOKEN, null)
        set(value) = customerPreferences.edit().putString(Constant.USER_TOKEN, value).apply()
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
        idUser = user.idUser ?: idUser
        idPelanggan = user.idPelanggan ?: idPelanggan
        nama = user.nama
        tempatLahir = user.tempatLahir
        tanggalLahir = user.tanggalLahir ?: tanggalLahir
        jenisKelamin = user.jenisKelamin ?: jenisKelamin
        alamat = user.alamat ?: alamat
        member = user.member.toString() ?: member
        langUser = (user.lang.toString()) ?: langUser
        longUser = (user.long.toString()) ?: longUser
        role = user.namaRole ?: role
        token = user.token ?: token
        isLoggedIn = true
        (user.desa ?: this.getDesa())?.let { saveDesa(it) }
    }

    fun getUser(): User? {
        return User(
            idUser = idUser,
            idPelanggan = idPelanggan,
            nama = nama!!,
            tempatLahir = tempatLahir!!,
            tanggalLahir = tanggalLahir!!,
            jenisKelamin = jenisKelamin!!,
            alamat = alamat!!,
            member = (if (member == "null") null else member?.toInt()) ?: 0,
            lang = (if (langUser !== "null") null else langUser?.toDouble()) ?: null,
            long = (if (longUser !== "null") null else longUser?.toDouble()) ?: null,
            namaRole = role,
            token = token.toString(),
            desa = getDesa() ?: null,
            username = null,
            password = null,
        )
    }

    fun saveDesa(desa: Desa) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val desaString = gson.toJson(desa)
        this.desa = desaString
    }

    fun getDesa(): Desa? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.fromJson(desa, Desa::class.java)
    }

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
