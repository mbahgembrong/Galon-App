package com.example.galonapps.ui.pelanggan.transaksi

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.App
import com.example.galonapps.R
import com.example.galonapps.databinding.ActivityAlamatBinding
import com.example.galonapps.databinding.ActivityTransaksiBinding
import com.example.galonapps.helper.LocationPermissionHelper
import com.example.galonapps.model.Desa
import com.example.galonapps.prefs
import com.example.galonapps.ui.login.LoginActivity
import com.example.galonapps.ui.pelanggan.PelangganActivity
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.*
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import www.sanju.motiontoast.MotionToast
import java.util.*

class AlamatActivity : AppCompatActivity(), OnMapClickListener {
    lateinit var binding: ActivityAlamatBinding
    private lateinit var mapView: MapView
    lateinit var userLocation: Point
    lateinit var viewModel: TransaksiViewModel
    var listDesa = ArrayList<Desa>()
    lateinit var desaClicked: Desa
    var newAddress: String? = null
    var alamatBaru: String? = null
    var newPoint: Point? = null
    var postCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        desaClicked = prefs.getDesa()!!
        initView()
        observer()
        mapView = binding.mapView
        mapBoxpermision()
        binding.buttonGantiAlamat2.setOnClickListener {
            App.alertDialog(this) {
                viewModel.updateAlamat(
                    desaClicked.id,
                    alamatBaru ?: prefs.alamat!!,
                    newPoint ?: Point.fromLngLat(prefs.longUser!!.toDouble(), prefs.langUser!!.toDouble())
                )
            }
        }
    }

    private fun observer() {
        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        viewModel.getListDesa.observe(this, androidx.lifecycle.Observer {
            it.let { it1 -> listDesa.addAll(it1) }
        })
        viewModel.getDesa()
        viewModel.isUpdated.observe(this, androidx.lifecycle.Observer {
            when (it) {
                200 -> {
                    toast("Berhasil Update", MotionToast.TOAST_SUCCESS)
                    onBackPressed()
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
    }

    private fun initView() {
        binding = ActivityAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.inputTextAlamat.setText(prefs?.alamat)
        binding.textDesaPelanggan.setText(prefs.getDesa()?.nama)

        binding.radioGroupAamat.setOnCheckedChangeListener { radioGroup, i ->
            var radioButton = radioGroup.findViewById<RadioButton>(radioGroup.getCheckedRadioButtonId())
            if (radioButton.text == "Tetap") {
                binding.inputTextAlamat.setText(prefs?.alamat)
                binding.textDesaPelanggan.setText(prefs.getDesa()?.nama)
                alamatBaru = null
                newPoint = null
                desaClicked = prefs.getDesa()!!
            } else {
                newPoint = userLocation
                alamatBaru = newAddress
                binding.inputTextAlamat.setText(newAddress)
                desaClicked = listDesa.find { it.kodePos == postCode }!!
                binding.textDesaPelanggan.setText(desaClicked.nama)
            }
        }
        binding.radioButton3.isChecked = true
    }

    fun toast(message: String, status: String) {
        MotionToast.createToast(
            this@AlamatActivity, message,
            status,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, com.example.galonapps.R.font.helvetica_regular)
        )
    }

    fun mapBoxpermision() = runWithPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION) {
        onMapReady()
    }

    //    mapbox
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }
    private var animator: ValueAnimator? = null

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
        userLocation = it
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        var addresses = geocoder.getFromLocation(userLocation.latitude(), userLocation.longitude(), 1)
        Timber.d("postal-code ${addresses[0].postalCode}")
        postCode = addresses[0].postalCode
        newAddress = addresses[0].getAddressLine(0)
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    //    function
    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent(true)
            setupGesturesListener()
        }
        mapView.getMapboxMap().addOnMapClickListener(this)
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent(enabled: Boolean) {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = enabled
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    baseContext,
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    baseContext,
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )

        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
        animator?.cancel()
        binding.mapView.getMapboxMap().removeOnMapClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapClick(point: Point): Boolean {
//        initLocationComponent(false)
//        Timber.d("point : $point")
//        val pointEvaluator = TypeEvaluator<Point> { fraction, startValue, endValue ->
//            Point.fromLngLat(
//                startValue.longitude() + fraction * (endValue.longitude() - startValue.longitude()),
//                startValue.latitude() + fraction * (endValue.latitude() - startValue.latitude())
//            )
//        }
//        animator = ValueAnimator().apply {
//            setObjectValues(userLocation, point)
//            setEvaluator(pointEvaluator)
//            addUpdateListener {
//                userLocation = it.animatedValue as Point
////                geojsonSource.geometry(it.animatedValue as Point)
//            }
//            duration = 2000
//            start()
//        }
//        userLocation = point
        return true
    }
}
