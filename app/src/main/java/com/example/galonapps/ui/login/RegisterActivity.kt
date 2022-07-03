package com.example.galonapps.ui.login

import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.databinding.ActivityRegisterBinding
import com.example.galonapps.model.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.datepicker.MaterialDatePicker
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var viewModel: AuthViewModel
    lateinit var dateData: String
    lateinit var listDesa: ArrayList<String>
    lateinit var jenisKelamin: String

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
                    UUID.randomUUID().toString(),
                    binding.inputTextNama.text.toString(),
                    binding.inputTextTempatLahir.text.toString(),
                    dateData,
                    jenisKelamin,
                    binding.inputTextAlamat.text.toString(),
                    lat,
                    lng,
                    0,
                    binding.inputTextDesa.text.toString(),
                    "pelanggan"
                )
            )
            finish()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel.isRegister.observe(this, androidx.lifecycle.Observer {
            if (it) {
                finish()
            }
        })
        viewModel.getListDesa.observe(this, androidx.lifecycle.Observer {
            listDesa = it as ArrayList<String>
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listDesa)
            binding.inputTextDesa.apply {
                setAdapter(adapter)
                setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        this.showDropDown()
                    }
                    false
                }
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

}
