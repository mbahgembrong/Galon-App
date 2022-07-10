package com.example.galonapps.network.service

import com.example.galonapps.model.ResponseData
import com.example.galonapps.model.User
import com.example.galonapps.prefs
import retrofit2.Call
import retrofit2.http.*

interface AuthService {
    @FormUrlEncoded
    @POST("api-login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("deviceName") deviceName: String? = "mobile",
    ): Call<ResponseData<User>>

    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("api-register")
    fun register(
        @Field("nama") nama: String,
        @Field("username") username: String,
        @Field("tempat_lahir") tempatLahir: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("alamat") alamat: String,
        @Field("desa") idDesa: String = "1",
        @Field("password") password: String,
        @Field("lang") lang: String?,
        @Field("long") long: String?,
        @Field("deviceName") deviceName: String? = "mobile",
    ): Call<ResponseData<User>>

    @POST("api-logout")
    fun logout(
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<Any>>

    @GET("api-update/{id}")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun edit(
        @Path("id") idUser: String,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}"
    ): Call<ResponseData<User>>

    @FormUrlEncoded
    @POST("api-update/{id}")
    fun update(
        @Path("id") idUser: String,
        @Field("nama") nama: String,
        @Field("tempat_lahir") tempatLahir: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("alamat") alamat: String,
        @Field("desa") idDesa: String? = "1",
        @Field("lang") lang: Double?,
        @Field("long") long: Double?,
        @Field("password") password: String?,
        @Field("username") username: String?,
        @Header("Authorization") token: String? = "Bearer ${prefs.token}",
    ): Call<ResponseData<List<User>>>
}
