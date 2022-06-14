package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Diskon(
    @SerializedName("id") var id:String,
    @SerializedName("nama_diskon") var namaDiskon:String,
    @SerializedName("jenis_potongan") var jenisPotongan:String,
    @SerializedName("diskon") var diskon:Int
) : Parcelable
