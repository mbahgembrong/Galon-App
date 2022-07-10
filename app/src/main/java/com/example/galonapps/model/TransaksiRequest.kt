package com.example.galonapps.model

import com.google.gson.annotations.SerializedName

data class TransaksiRequest(
    @SerializedName("id") val id: String?,
    @SerializedName("pelanggan") val idPelanggan: String?,
    @SerializedName("kurir") val idKurir: String?,
    @SerializedName("detail_transaksi") val detailTransaksi: List<CartItem>?,
    @SerializedName("file_image") val fileImage: String?,
    @SerializedName("status") val statusTransaksi: Int?,
)
