package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Desa(
    @SerializedName("id") var id: String,
    @SerializedName("nama_desa") var nama: String,
    @SerializedName("ongkir") var ongkir: Int,
    @SerializedName("kode_pos") var kodePos: String?,
) : Parcelable
