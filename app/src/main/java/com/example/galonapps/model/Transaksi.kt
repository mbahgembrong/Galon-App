package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaksi(
    @SerializedName("id") var id:String,
    @SerializedName("pelanggan") var pelanggan: Pelanggan?,
    @SerializedName("karyawan") var karyawan: Karyawan? =null,
    @SerializedName("kurir") var kurir: Kurir? =null,
    @SerializedName("diskon") var diskon: Diskon? =null,
    @SerializedName("total") var total:Int,
    @SerializedName("status") var status:Int,
    @SerializedName("keterangan") var keterangan: String?,
    @SerializedName("detail_transaksi") var detailTransaksi: MutableList<DetailTransaksi>,
    @SerializedName("created_at") var createdAt: String?,
) : Parcelable
