package com.example.galonapps.ui.kurir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.adapter.OrderKurirAdapter
import com.example.galonapps.databinding.ActivityLaporanBinding
import com.example.galonapps.model.LaporanKurir
import com.example.galonapps.model.Transaksi
import com.example.galonapps.ui.kurir.maps.MapsActivity
import timber.log.Timber

class LaporanActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_LAPORAN = "extra_laporan"
    }

    lateinit var viewModel: KurirViewModel
    lateinit var binding: ActivityLaporanBinding
    lateinit var orderKurirAdapter: OrderKurirAdapter
    private var orderKurirList: ArrayList<Transaksi> = ArrayList()
    lateinit var laporan: LaporanKurir
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
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
        viewModel.getOrderKurir(statusToInt(laporan.nama))
    }

    private fun initView() {
        laporan = intent.getParcelableExtra<LaporanKurir>(EXTRA_LAPORAN)!!
        binding = ActivityLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textNama.setText("Laporan ${laporan.nama}")
        setupRecycleView()
    }

    private fun setupRecycleView() {
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
        binding.rvListLaporanKurir.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvListLaporanKurir.adapter = orderKurirAdapter

    }

    private fun statusToInt(status: String): Int {
        return when (status) {
            "Jumlah Belum Terkirim" -> 3
            "Jumlah Terkirim" -> 4
            "Jumlah Pesanan Dibatalkan" -> 5
            else -> 0
        }
    }
}
