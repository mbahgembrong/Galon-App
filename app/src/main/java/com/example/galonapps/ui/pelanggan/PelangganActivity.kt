package com.example.galonapps.ui.pelanggan

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.galonapps.R
import com.example.galonapps.databinding.ActivityPelangganBinding
import com.example.galonapps.ui.pelanggan.transaksi.TransaksiActivity

class PelangganActivity : AppCompatActivity() {


    private var binding: ActivityPelangganBinding? = null
    val bind get() = binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        binding?.buttonCartPelanggan?.setOnClickListener {
            val intent = Intent(this, TransaksiActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initView() {
        binding = ActivityPelangganBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val navView: BottomNavigationView = binding!!.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_pelanggan)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_order, R.id.navigation_profile
            )
        )
//        setupActionBarWithNavController(navController, ])
        navView.setupWithNavController(navController)
    }
}
