package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id_user") var idUser: String?,
    @SerializedName("id") var idPelanggan: String?,
    @SerializedName("nama") var nama: String,
    @SerializedName("tempat_lahir") var tempatLahir: String,
    @SerializedName("tanggal_lahir") var tanggalLahir: String,
    @SerializedName("jenis_kelamin") var jenisKelamin: String,
    @SerializedName("alamat") var alamat: String,
    @SerializedName("lang") var lang: Double?,
    @SerializedName("long") var long: Double?,
    @SerializedName("password") var password: String?,
    @SerializedName("username") var username: String?,
    @SerializedName("desa") var desa: Desa?,
    @SerializedName("member") var member: Int?,
    @SerializedName("nama_role") var namaRole: String?,
    @SerializedName("token") var token: String?,
) : Parcelable {

}
