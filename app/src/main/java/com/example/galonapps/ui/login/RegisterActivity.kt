package com.example.galonapps.ui.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.databinding.ActivityRegisterBinding
import com.example.galonapps.model.Desa
import com.example.galonapps.model.User
import com.example.galonapps.ui.pelanggan.PelangganActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import www.sanju.motiontoast.MotionToast
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var viewModel: AuthViewModel
    var dateData: String = ""
    var listDesa = ArrayList<Desa>()
    var jenisKelamin: String = ""
    var desaClicked: Desa? = null
    var lat: Double = 0.0;
    var lng: Double = 0.0;
    var airLoc: AirLocation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setObserver()
        getLocation()
        viewModel.getDesa()
        binding.radioJeniskelamin.setOnCheckedChangeListener { radioGroup, i ->
            var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            if (radioButton.text == "Perempuan") {
                jenisKelamin = "P"
            } else {
                jenisKelamin = "L"
            }
        }
        binding.buttonRegister.setOnClickListener {
            viewModel.register(
                User(
                    null,
                    null,
                    binding.inputTextNama.text.toString(),
                    binding.inputTextTempatLahir.text.toString(),
                    dateData,
                    jenisKelamin,
                    binding.inputTextAlamat.text.toString(),
                    lat,
                    lng,
                    binding.inputTextPassword2.text.toString(),
                    binding.inputTextUsername.text.toString(),
                    desaClicked,
                    null,
                    null,
                    null,
                )
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setObserver() {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel.isRegistered.observe(this, androidx.lifecycle.Observer {
            when (it) {
                200 -> {
                    toast("Berhasil Register", MotionToast.TOAST_SUCCESS)
                    val intent = Intent(this, PelangganActivity::class.java)
                    startActivity(intent).also { finish() }
                }
                205 -> {
                    toast(
                        "Ada data yang kosong",
                        MotionToast.TOAST_WARNING,
                    )
                }
                500 -> {
//                    toast(
//                        "Akun sudah terdaftar, tolong ganti nama akun",
//                        MotionToast.TOAST_INFO,
//                    )
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent).also { finish() }
                }
                404 -> {
                    toast(
                        "Periksa koneksi anda",
                        MotionToast.TOAST_NO_INTERNET,
                    )
                }
                else -> {
                    toast(
                        "Register Failed",
                        MotionToast.TOAST_ERROR,
                    )
                }
            }
        })
        viewModel.getListDesa.observe(this, androidx.lifecycle.Observer {
            it.let { it1 -> listDesa.addAll(it1) }
            val desas = ArrayList<String>()
            for (i in listDesa) {
                desas.add(i.nama)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, desas)
            binding.inputTextDesa.apply {
                setAdapter(adapter)
                setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        this.showDropDown()
                    }
                    false
                }
            }
            binding.inputTextDesa.setOnItemClickListener { parent, view, position, id ->
                desaClicked = listDesa[position]
//                binding.inputTextDesa.setText(desaClicked?.nama)
            }
        })
    }

    fun getLocation() {
        airLoc = AirLocation(this, true, true,
            object : AirLocation.Callbacks {
                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                    Toast.makeText(
                        applicationContext, "Gagal mendapatkan posisi saat ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess(location: Location) {
                    lat = location.latitude
                    lng = location.longitude
                    Timber.e("lat: $lat, lng: $lng")
                    val geocoder = Geocoder(applicationContext, Locale.getDefault())
                    var addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    binding.inputTextAlamat.setText(addresses[0].getAddressLine(0))
                }
            })
    }

    private fun initView() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        binding.inputTextUsername.setText(username)
        binding.inputTextPassword2.setText(password)

        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        binding.buttonTanggalLahir.setOnClickListener {
            datePicker.show(supportFragmentManager, datePicker.toString())
            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                dateData = dateFormatter.format(Date(it))
                binding.textTanggalLahir.apply {
                    text = "Tanggal Lahir : $dateData"
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLoc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        airLoc?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun toast(message: String, status: String) {
        MotionToast.createToast(
            this@RegisterActivity, message,
            status,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, com.example.galonapps.R.font.helvetica_regular)
        )
    }
}
