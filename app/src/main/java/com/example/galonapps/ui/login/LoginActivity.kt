package com.example.galonapps.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.R
import com.example.galonapps.databinding.ActivityLoginBinding
import com.example.galonapps.prefs
import com.example.galonapps.ui.kurir.KurirActivity
import com.example.galonapps.ui.pelanggan.PelangganActivity
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import www.sanju.motiontoast.MotionToast

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setObserver()
        binding.buttonLogin.setOnClickListener {
            viewModel.login(
                binding.inputTextUsernameLogin.text.toString(),
                binding.inputTextPasswordLogin.text.toString()
            )
        }
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        isLoggedIn()
    }

    private fun isLoggedIn() {
        if (prefs.isLoggedIn) {
            if (prefs.role == "kurir") {
                startActivity(Intent(this, KurirActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, PelangganActivity::class.java))
                finish()
            }
        }
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.islogined.observe(this) {
            when (it) {
                200 -> {
                    isLoggedIn()
                }
                205 -> {
                    toast(
                        "Username / Password salah",
                        MotionToast.TOAST_WARNING,
                    )
                }
                500 -> {
                    toast(
                        "Anda Belum Terdaftar",
                        MotionToast.TOAST_INFO,
                    )
//                    AlertDialog.Builder(this)
//                        .setTitle("Akun anda belum register")
//                        .setMessage("Apakah anda mau registrasi ?")
//                        .setPositiveButton("OK") { dialog, which ->
//                            val intent = Intent(this, RegisterActivity::class.java)
//                            intent.putExtra("username", binding.inputTextUsernameLogin.text.toString())
//                            intent.putExtra("password", binding.inputTextPasswordLogin.text.toString())
//                            startActivity(intent)
//                            finish()
//                        }
//                        .setNegativeButton("Cancel") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
                }
                404 -> {
                    toast(
                        "Periksa koneksi anda",
                        MotionToast.TOAST_NO_INTERNET,
                    )
                }
                else -> {
                    toast(
                        "Login Failed",
                        MotionToast.TOAST_ERROR,
                    )
                }
            }
        }
    }

    fun toast(message: String, status: String) {
        MotionToast.createToast(
            this, message,
            status,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular)
        )
    }

    private fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
