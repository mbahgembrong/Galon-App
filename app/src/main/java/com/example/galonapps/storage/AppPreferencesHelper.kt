package com.example.galonapps.storage

interface AppPreferencesHelper {
    val id: String?
    val nama: String?
    val tempatLahir:String?
    val jenisKelamin:String?
    val alamat: String?
    val member:String?
    val desa:String?
    val langUser: String?
    val longUser: String?
    val role: String?
    val idPelanggan:String?
    val langPelanggan:String?
    val longPelanggan:String?
    val cart: String?

    fun clearPreferences()

    fun clearCartPreferences()
}
