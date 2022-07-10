package com.example.galonapps.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ResponseData<out T>(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: T?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("messages")
    val messages: Any?,
    @SerializedName("message")
    val message: String?
)
