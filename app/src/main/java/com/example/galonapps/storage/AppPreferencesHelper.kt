package com.example.galonapps.storage

interface AppPreferencesHelper {
    abstract var isLoggedIn: Boolean
    val idUser: String?
    val idPelanggan: String?
    val nama: String?
    val tempatLahir: String?
    val tanggalLahir: String?
    val jenisKelamin: String?
    val alamat: String?
    val member: String?
    val desa: String?
    val langUser: String?
    val longUser: String?
    val role: String?
    val token: String?
    val cart: String?

    fun clearPreferences()

    fun clearCartPreferences()
}
