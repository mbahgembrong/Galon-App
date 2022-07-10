package com.example.galonapps.network.service

import com.example.galonapps.model.Galon
import com.example.galonapps.model.ResponseData
import com.example.galonapps.prefs
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface GalonService {
    @GET("api-galon")
    @Headers("Accept:application/json", "Content-Type:application/json")

    fun galon(@Header("Authorization") token: String? = "Bearer ${prefs.token}"): Call<ResponseData<List<Galon>>>
}
