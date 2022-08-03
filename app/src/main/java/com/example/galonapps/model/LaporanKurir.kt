package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaporanKurir(
    @SerializedName("nama") val nama: String,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("icon") val icon: String
) : Parcelable
