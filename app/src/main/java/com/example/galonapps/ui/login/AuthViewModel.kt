package com.example.galonapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.galonapps.model.Galon
import com.example.galonapps.model.User
import com.example.galonapps.prefs
import java.util.*

class AuthViewModel : ViewModel() {
    private val _isLoggedIn = MutableLiveData<Int>()
    val isLoggedIn: LiveData<Int>
        get() = _isLoggedIn
    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean>
        get() = _isRegister
    private val _listDesa = MutableLiveData<List<String>>()
    val getListDesa: LiveData<List<String>>
        get() = _listDesa

    fun login(username: String, password: String) {
        if (username == "kurir" && password == "kurir") {
            prefs.saveUser(
                User(
                    UUID.randomUUID().toString(),
                    "kurir",
                    "kediri",
                    tanggalLahir = Date().toString(),
                    "1",
                    "Jabon",
                    null,
                    null,
                    null,
                    null,
                    "kurir"
                )
            )
            _isLoggedIn.value = 200

        }
        if (username == "pelanggan" && password == "pelanggan") {
            prefs.saveUser(
                User(
                    UUID.randomUUID().toString(),
                    "pelanggan",
                    "kediri",
                    tanggalLahir = Date().toString(),
                    "1",
                    "kediri",
                    null,
                    null,
                    null,
                    "Kediri",
                    "pelanggan"
                )
            )
            _isLoggedIn.value = 200
        } else
            _isLoggedIn.value = 404

    }

    fun register(user: User) {
        prefs.saveUser(user)
        _isRegister.value = true
    }

    fun getDesa() {
        _listDesa.value = mutableListOf(
            "Kediri",
            "jabon",
            "Banyakan"
        )
    }

}
