package com.example.galonapps.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Transaksi(
    @SerializedName("id") var id: String,
    @SerializedName("pelanggan") var pelanggan: User?,
    @SerializedName("karyawan") var karyawan: User?,
    @SerializedName("kurir") var kurir: User?,
    @SerializedName("diskon") var diskon: Int?,
    @SerializedName("ongkir") var ongkir: Int?,
    @SerializedName("total") var total: Int,
    @SerializedName("status") var status: Int,
    @SerializedName("keterangan") var keterangan: String?,
    @SerializedName("detail_transaksi") var detailTransaksi: List<DetailTransaksi>,
    @SerializedName("bukti_transaksi") var buktiTransaksi: String?,
    @SerializedName("alamat") var alamat: String?,
    @SerializedName("lang") var lang: String? = "0.0",
    @SerializedName("long") var long: String? = "0.0",
    @SerializedName("desa") var desa: Desa?,
    @SerializedName("created_at") var createdAt: String?,
    var jarak: Double?,
) : Parcelable
