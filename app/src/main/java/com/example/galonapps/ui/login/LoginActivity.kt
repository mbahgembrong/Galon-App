package com.example.galonapps.ui.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.R
import com.example.galonapps.databinding.ActivityLoginBinding
import com.example.galonapps.prefs
import com.example.galonapps.ui.kurir.KurirActivity
import com.example.galonapps.ui.main.MainActivity
import com.example.galonapps.ui.pelanggan.PelangganActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setObserver()
        binding.buttonLogin.setOnClickListener {
          viewModel.login(binding.inputTextUsernameLogin.text.toString(), binding.inputTextPasswordLogin.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        isLoggedIn()
    }
    private fun isLoggedIn() {
        if (prefs.isLoggedIn) {
            if (prefs.role == "kurir"){
                startActivity(Intent(this, KurirActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, PelangganActivity::class.java))
                finish()
            }
        }
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.isLoggedIn.observe(this) {
            when (it) {
                200 -> {
                   isLoggedIn()
                }
                404 ->{
                    AlertDialog.Builder(this)
                        .setTitle("Akun anda belum register")
                        .setMessage("Apakah anda mau registrasi ?")
                        .setPositiveButton("OK") { dialog, which ->
                            val intent = Intent(this, RegisterActivity::class.java)
                            intent.putExtra("username", binding.inputTextUsernameLogin.text.toString())
                            intent.putExtra("password", binding.inputTextPasswordLogin.text.toString())
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    private fun initView() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
