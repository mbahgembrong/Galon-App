package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pelanggan(
    @SerializedName("id") var id:String,
    @SerializedName("nama") var nama:String,
    @SerializedName("tempat_lahir") var tanggalLahir:String,
    @SerializedName("jenis_kelamin") var jenisKelamin:String,
    @SerializedName("alamat") var alamat:String,
    @SerializedName("id_user") var idUser:Int,
    @SerializedName("lang") var lang:Int,
    @SerializedName("long") var long:Int,
    @SerializedName("member") var member:Int,
    @SerializedName("desa") var desa:String
) : Parcelable
