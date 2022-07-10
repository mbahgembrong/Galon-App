package com.example.galonapps.ui.pelanggan.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.model.Desa
import com.example.galonapps.model.ResponseData
import com.example.galonapps.model.User
import com.example.galonapps.network.ApiConfig
import com.example.galonapps.network.service.AuthService
import com.example.galonapps.network.service.DesaService
import com.example.galonapps.prefs
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ProfileViewModel : ViewModel() {
    private val _listDesa = MutableLiveData<List<Desa>>()
    val getListDesa: LiveData<List<Desa>>
        get() = _listDesa
    private val _user = MutableLiveData<User>()
    val getUser: LiveData<User>
        get() = _user

    fun getDesa() {
        val retroInstance = ApiConfig.getRetroInstance().create(DesaService::class.java)
        val call = retroInstance.desa()
        call.enqueue(object : Callback<ResponseData<List<Desa>>> {
            override fun onResponse(
                call: Call<ResponseData<List<Desa>>>,
                response: Response<ResponseData<List<Desa>>>
            ) {
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        _listDesa.value = response.body()?.data
                    } else {
                        _listDesa.value = null
                    }
                else {
                    _listDesa.value = null
                }
            }

            override fun onFailure(call: Call<ResponseData<List<Desa>>>, t: Throwable) {
                _listDesa.value = null
            }

        })
    }

    fun updateUser(user: User) {
        val retroInstance = ApiConfig.getRetroInstance().create(AuthService::class.java)
        val call = retroInstance.update(
            prefs.idUser!!,
            user.nama,
            user.tempatLahir!!,
            user.tanggalLahir!!,
            user.jenisKelamin!!,
            user.alamat,
            user.desa?.id,
            user.lang,
            user.long,
            user.password,
            user.username
        )
        call.enqueue(object : Callback<ResponseData<List<User>>?> {
            override fun onResponse(
                call: Call<ResponseData<List<User>>?>,
                response: Response<ResponseData<List<User>>?>
            ) {
                Timber.d("onResponse: ${response.body()}")
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        response.body()?.data?.get(0)?.let { prefs.saveUser(it) }
                        _user.value = prefs.getUser()
                    } else {
                        _user.value = prefs.getUser()
                    }
                else {
                    _user.value = prefs.getUser()
                }
            }

            override fun onFailure(call: Call<ResponseData<List<User>>?>, t: Throwable) {
                _user.value = prefs.getUser()
            }

        })
    }
}
