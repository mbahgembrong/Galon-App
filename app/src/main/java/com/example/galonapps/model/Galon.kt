package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Galon(
    @SerializedName("id") var id:String,
    @SerializedName("merk") var merk:String,
    @SerializedName("isi_galon") var isiGalon:String,
    @SerializedName("jml_stok") var stok:Int,
    @SerializedName("harga_awal") var hargaAwal:Int,
    @SerializedName("harga_jual") var hargaJual:Int,
    @SerializedName("image") var image:String
) : Parcelable
