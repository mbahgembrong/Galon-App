package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailTransaksi(
    @SerializedName("id") var id:String,
    @SerializedName("id_transaksi") var idTransaksi: String,
    @SerializedName("id_galon") var idGalon:String,
    @SerializedName("jumlah") var jumlah:Int,
    @SerializedName("total_harga") var totalHarga:Int
) : Parcelable
