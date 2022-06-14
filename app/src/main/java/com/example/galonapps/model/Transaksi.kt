package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaksi(
    @SerializedName("id") var id:String,
    @SerializedName("pelanggan") var pelanggan:Pelanggan,
    @SerializedName("karyawan") var karyawan:Karyawan,
    @SerializedName("kurir") var kurir:Kurir,
    @SerializedName("diskon") var diskon:Diskon,
    @SerializedName("total") var total:Int,
    @SerializedName("status") var status:Int,
    @SerializedName("keterangan") var keterangan:String,
    @SerializedName("detail_transaksi") var detailTransaksi: DetailTransaksi
) : Parcelable
