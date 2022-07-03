package com.example.galonapps.ui.pelanggan.transaksi

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.galonapps.R
import com.example.galonapps.databinding.ActivityAlamatBinding
import com.example.galonapps.databinding.ActivityTransaksiBinding
import com.example.galonapps.helper.LocationPermissionHelper
import com.google.android.gms.maps.OnMapReadyCallback
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
import timber.log.Timber
import java.util.*

class AlamatActivity : AppCompatActivity(), OnMapClickListener {
    lateinit var binding: ActivityAlamatBinding
    private lateinit var mapView: MapView
    lateinit var userLocation: Point
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observer()
        mapView = binding.mapView
        onMapReady()
    }

    private fun observer() {

    }

    private fun initView() {
        binding = ActivityAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        Timber.d("alamat : ${addresses[0].postalCode}")
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
