package com.example.galonapps.model

import com.google.gson.annotations.SerializedName

data class LaporanKurir(
    @SerializedName("nama") val nama: String,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("icon") val icon: String
)
