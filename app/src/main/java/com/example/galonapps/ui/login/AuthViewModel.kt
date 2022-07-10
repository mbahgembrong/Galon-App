package com.example.galonapps.ui.login

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class AuthViewModel : ViewModel() {
    private val _isLogined = MutableLiveData<Int>()
    val islogined: LiveData<Int>
        get() = _isLogined
    private val _isRegistered = MutableLiveData<Int>()
    val isRegistered: LiveData<Int>
        get() = _isRegistered
    private val _listDesa = MutableLiveData<List<Desa>>()
    val getListDesa: LiveData<List<Desa>>
        get() = _listDesa

    fun login(username: String, password: String) {
        val retroInstance = ApiConfig.getRetroInstance().create(AuthService::class.java)
        val call = retroInstance.login(username, password)
        call.enqueue(object : Callback<ResponseData<User>> {
            override fun onResponse(
                call: Call<ResponseData<User>>,
                response: Response<ResponseData<User>>
            ) {
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        response.body()?.data?.let { prefs.saveUser(it) }
                        prefs.token = response.body()?.token
                        _isLogined.value = 200

                    } else {

                        _isLogined.value = 205
                    }
                else {

                    _isLogined.value = 500
                }

            }

            override fun onFailure(call: Call<ResponseData<User>>, t: Throwable) {
                Timber.e(t)
                _isLogined.value = 404
            }

        })
    }

    fun register(user: User) {
        Timber.d("register : ${user.toString()}")
        val retroInstance = ApiConfig.getRetroInstance().create(AuthService::class.java)
        val call = retroInstance.register(
            user.nama,
            user.username!!,
            user.tempatLahir,
            user.tanggalLahir,
            user.jenisKelamin,
            user.alamat,
            user.desa?.id ?: "1",
            user.password!!,
            user.lang.toString(),
            user.long.toString(),
        )
        call.enqueue(object : Callback<ResponseData<User>?> {
            override fun onResponse(
                call: Call<ResponseData<User>?>,
                response: Response<ResponseData<User>?>
            ) {
                Timber.d("onResponse: ${response.body()}")
                if (response.isSuccessful)
                    if (response.body()?.status == true) {
                        response.body()?.data?.let { prefs.saveUser(it) }
                        prefs.token = response.body()?.token
                        _isRegistered.value = 200
                        Timber.d("message 1: ${response.body()}")
                    } else {
                        _isRegistered.value = 205
                        Timber.d("message 2: ${response}")
                    }
                else {
                    Timber.d("message 3: ${response}")
                    Timber.d("message 3: ${call}")
                    _isRegistered.value = 500
                }
            }

            override fun onFailure(call: Call<ResponseData<User>?>, t: Throwable) {
                Timber.d("message 4: ${t}")
                _isRegistered.value = 404
            }

        })
    }

    fun getDesa() {
        val retroInstance = ApiConfig.getRetroInstance().create(DesaService::class.java)
        val call = retroInstance.desa()
        call.enqueue(object : Callback<ResponseData<kotlin.collections.List<Desa>>> {
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

}
