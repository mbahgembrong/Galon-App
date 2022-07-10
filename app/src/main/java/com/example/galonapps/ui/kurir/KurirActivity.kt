package com.example.galonapps.ui.kurir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galonapps.App
import com.example.galonapps.R
import com.example.galonapps.adapter.LaporanKurirAdapter
import com.example.galonapps.adapter.OrderKurirAdapter
import com.example.galonapps.databinding.ActivityKurirBinding
import com.example.galonapps.model.LaporanKurir
import com.example.galonapps.model.Transaksi
import com.example.galonapps.prefs
import com.example.galonapps.ui.kurir.maps.MapsActivity
import com.example.galonapps.ui.login.LoginActivity
import com.example.galonapps.ui.pelanggan.order.OrderDetailActivity
import timber.log.Timber

class KurirActivity : AppCompatActivity() {
    lateinit var viewModel: KurirViewModel
    lateinit var binding: ActivityKurirBinding
    lateinit var orderKurirAdapter: OrderKurirAdapter
    private var orderKurirList: ArrayList<Transaksi> = ArrayList()
    lateinit var laporanKurirAdapter: LaporanKurirAdapter
    private var laporanKurirList: ArrayList<LaporanKurir> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setObserver()
        binding.buttonLogOut.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah anda yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, which ->
                    dialog.dismiss()
                    viewModel.logout()
                    prefs.isLoggedIn = false
                    prefs.clearPreferences()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent).also { this.finish() }
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun onResume() {
        super.onResume()
        setObserver()
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(KurirViewModel::class.java)
        viewModel.getOrderKurirList.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Timber.d("getOrderKurirList: $it")
                orderKurirList.clear()
                orderKurirList.addAll(it)
                orderKurirAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getOrderKurir()
        viewModel.getLaporanKurirList.observe(this, Observer {
            if (it != null) {
                Timber.d("getLaporanKurirList: $it")
                laporanKurirList.clear()
                laporanKurirList.addAll(it)
                laporanKurirAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getLaporanKurir()
        viewModel.isLogout.observe(this, Observer<Boolean> {
            if (it == true) {

            }
        })
    }

    private fun initView() {
        binding = ActivityKurirBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.namaKurir.text = prefs.nama
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        orderKurirAdapter =
            OrderKurirAdapter(this, orderKurirList, object : OrderKurirAdapter.OnItemClickListener {
                override fun onClickListener(position: Int) {
                    val intent = Intent(baseContext, DetailOrderKurirActivity::class.java)
                    intent.putExtra(DetailOrderKurirActivity.ORDER_DETAIL_ACTIVITY, orderKurirList[position])
                    startActivity(intent)
                }

                override fun onDriverClickListener(position: Int) {
                    val intent = Intent(baseContext, MapsActivity::class.java)
                    intent.putExtra(MapsActivity.MAPS_ACTIVITY_KEY, orderKurirList[position])
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent).also { finish() }
                }
            })
        binding.rvListOrderKurir.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvListOrderKurir.adapter = orderKurirAdapter
        laporanKurirAdapter =
            LaporanKurirAdapter(this, laporanKurirList)
        binding.rvLaporan.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvLaporan.adapter = laporanKurirAdapter

    }
}
