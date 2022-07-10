package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Desa(
    @SerializedName("id") val id: String,
    @SerializedName("nama_desa") val nama: String,
    @SerializedName("ongkir") val ongkir: Int,
) : Parcelable
