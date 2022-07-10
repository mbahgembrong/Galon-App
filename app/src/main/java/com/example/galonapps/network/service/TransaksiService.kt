package com.example.galonapps.network.service

import com.example.galonapps.model.*
import com.example.galonapps.prefs
import retrofit2.Call
import retrofit2.http.*

interface TransaksiService {
    @GET("api-transaksi/pelanggan/transaksi/")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun indexPelanggan(
        @Query("pelanggan_id") idPelanggan: String,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<List<Transaksi>>>

    @POST("api-transaksi/pelanggan/transaksi/create")
    fun createPelanggan(
        @Body transaksiRequest: TransaksiRequest,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<Transaksi>>

    @POST("api-transaksi/pelanggan/transaksi/update")
    fun updatePelanggan(
        @Body transaksiRequest: TransaksiRequest,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<Transaksi?>?>

    @GET("api-transaksi/kurir/order")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun indexKurir(
        @Query("kurir_id") idKurir: String,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<List<Transaksi>>>

    @POST("api-transaksi/kurir/order")
    fun updateKurir(
        @Body transaksiRequest: TransaksiRequest,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<Transaksi?>?>

    @GET("api-laporan-kurir/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun laporanKurir(
        @Path("id") idKurir: String,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<List<LaporanKurir>>>
}
