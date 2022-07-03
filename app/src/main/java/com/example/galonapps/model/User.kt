package com.example.galonapps.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id:String,
    @SerializedName("nama") var nama:String,
    @SerializedName("tempat_lahir") var tempatLahir:String,
    @SerializedName("tanggal_lahir") var tanggalLahir:String,
    @SerializedName("jenis_kelamin") var jenisKelamin:String,
    @SerializedName("alamat") var alamat:String,
    @SerializedName("lang") var lang:Double?,
    @SerializedName("long") var long:Double?,
    @SerializedName("member") var member:Int?,
    @SerializedName("desa") var desa:String?,
    @SerializedName("role") var role:String?
) : Parcelable
