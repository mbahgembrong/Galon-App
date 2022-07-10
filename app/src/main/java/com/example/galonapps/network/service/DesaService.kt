package com.example.galonapps.network.service

import com.example.galonapps.model.Desa
import com.example.galonapps.model.Galon
import com.example.galonapps.model.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface DesaService {
    @GET("api-desa")
    @Headers("Accept:application/json", "Content-Type:application/json")
    fun desa(): Call<ResponseData<List<Desa>>>
}
