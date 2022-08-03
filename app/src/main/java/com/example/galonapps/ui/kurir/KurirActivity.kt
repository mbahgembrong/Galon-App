package com.example.galonapps.ui.kurir

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galonapps.R
import com.example.galonapps.adapter.LaporanKurirAdapter
import com.example.galonapps.adapter.OrderKurirAdapter
import com.example.galonapps.databinding.ActivityKurirBinding
import com.example.galonapps.databinding.ActivityPelangganBinding
import com.example.galonapps.model.LaporanKurir
import com.example.galonapps.model.Transaksi
import com.example.galonapps.prefs
import com.example.galonapps.ui.kurir.maps.MapsActivity
import com.example.galonapps.ui.login.LoginActivity
import com.example.galonapps.ui.pelanggan.transaksi.TransaksiActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.geojson.Point
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class KurirActivity : AppCompatActivity() {
    private var binding: ActivityKurirBinding? = null
    val bind get() = binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    private fun initView() {
        binding = ActivityKurirBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val navView: BottomNavigationView = binding!!.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_kurir)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_profile
            )
        )
//        setupActionBarWithNavController(navController, ])
        navView.setupWithNavController(navController)
    }
}
