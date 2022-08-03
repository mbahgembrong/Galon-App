package com.example.galonapps.ui.kurir

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.App
import com.example.galonapps.databinding.FragmentProfileBinding
import com.example.galonapps.model.Desa
import com.example.galonapps.model.User
import com.example.galonapps.prefs
import com.example.galonapps.ui.pelanggan.profile.ProfileViewModel
import com.example.galonapps.ui.pelanggan.transaksi.TransaksiViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.mapbox.geojson.Point
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import www.sanju.motiontoast.MotionToast
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    lateinit var userLocation: Point
    var newAddress: String? = null
    var alamatBaru: String? = null
    var newPoint: Point? = null
    var dateData: String = ""
    var jenisKelamin: String = ""
    var airLoc: AirLocation? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observer()
        binding.buttonUpdate.setOnClickListener {
            App.alertDialog(requireContext(), "Apakah anda yakin ingin mengubah data?") {
                viewModel.updateUser(
                    User(
                        prefs.idUser,
                        null,
                        binding.inputTextNama3.text.toString(),
                        binding.inputTextTempatLahir3.text.toString(),
                        dateData,
                        jenisKelamin,
                        alamatBaru ?: prefs.alamat!!,
                        null,
                        null,
                        if (!binding.inputTextPassword3.text.isNullOrEmpty()) binding.inputTextPassword3.text.toString() else null,
                        if (!binding.inputTextUsername3.text.isNullOrEmpty()) binding.inputTextUsername3.text.toString() else null,
                        Desa("0", "aaa", 11),
                        null,
                        null,
                        prefs.token
                    )
                )
            }
        }
    }

    private fun initView() {
        binding.inputTextNama3.setText(prefs.nama)
        binding.inputTextTempatLahir3.setText(prefs.tempatLahir)
        binding.textTanggalLahir.apply {
            dateData = prefs.tanggalLahir!!
            visibility = View.VISIBLE
            text = prefs.tanggalLahir
        }
        if (prefs.jenisKelamin == "P") {
            binding.radioPerempuan.isChecked = true
            jenisKelamin = "P"
        } else {
            binding.radioLakiLaki.isChecked = true
            jenisKelamin = "L"
        }
        binding.radioJeniskelamin.setOnCheckedChangeListener { radioGroup, i ->
            var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            if (radioButton.text == "Perempuan") {
                jenisKelamin = "P"
            } else {
                jenisKelamin = "L"
            }
        }
        binding.apply {
            textView30.visibility = View.GONE
            inputTextDesa3.visibility = View.GONE
        }
        binding.inputTextAlamat3.setText(prefs.alamat)
        binding.radioGroupAamat.setOnCheckedChangeListener { radioGroup, i ->
            var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            if (radioButton.text == "Tetap") {
                binding.inputTextAlamat3.setText(prefs?.alamat)
                alamatBaru = null
                newPoint = null
            } else {
                getLocation()
                newPoint = userLocation
                alamatBaru = newAddress
                binding.inputTextAlamat3.setText(newAddress)
            }
        }
        getLocation()
        binding.radioButton3.isChecked = true
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        binding.buttonTanggalLahir.setOnClickListener {
            datePicker.show(activity?.supportFragmentManager!!, datePicker.toString())
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

    fun getLocation() {
        airLoc = AirLocation(requireActivity(), true, true,
            object : AirLocation.Callbacks {
                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                    Toast.makeText(
                        requireContext(), "Gagal mendapatkan posisi saat ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess(location: Location) {
                    userLocation = Point.fromLngLat(location.longitude, location.latitude)
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    var addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    newAddress = addresses[0].getAddressLine(0)
                }
            })
    }

    private fun observer() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.getUser.observe(this) {
            if (it != null)
                toast("Berhasil Merubah Data", MotionToast.TOAST_SUCCESS)
            else
                toast("Gagal Merubah Data", MotionToast.TOAST_ERROR)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun toast(message: String, status: String) {
        MotionToast.createToast(
            requireActivity(), message,
            status,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(requireContext(), com.example.galonapps.R.font.helvetica_regular)
        )
    }
}
