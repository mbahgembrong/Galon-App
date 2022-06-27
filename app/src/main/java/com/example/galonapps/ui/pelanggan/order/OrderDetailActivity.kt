package com.example.galonapps.ui.pelanggan.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.R
import com.example.galonapps.adapter.GalonOrderDetailAdapter
import com.example.galonapps.databinding.ActivityOrderDetailBinding
import com.example.galonapps.model.Transaksi

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var galonAdapter: GalonOrderDetailAdapter
    private lateinit var transaksi: Transaksi
    private lateinit var orderDetailViewModel: OrderViewModel
    companion object{
        val ORDER_DETAIL_ACTIVITY = "order_detail_activity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaksi = intent.getParcelableExtra(ORDER_DETAIL_ACTIVITY)!!
        initView()
        setObservers()
        binding.buttonCancelOrderDetail.setOnClickListener {
            if (orderDetailViewModel.cancelOrder(transaksi.id))
                finish()
        }
        binding.buttonBayarOrderDetail.setOnClickListener {
            if (orderDetailViewModel.bayarOrder(transaksi.id))
                finish()
        }
    }


    private fun initView() {
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textStatusOrderDetail.text = "Pesanan ${statusToString(transaksi.status)}"
        binding.textKeteranganOrderDetail.text = transaksi.keterangan?:""
        binding.textKaryawanOrderDetail.text = "Karyawan : ${transaksi.karyawan ?: "-"}"
        binding.textKurirOrderDetail.text = "Kurir : ${transaksi.kurir ?: "-"}"
        binding.textNamaPelangganOrderDetail.text = "Tanggal : ${transaksi.pelanggan?:"-"}"
        binding.textGrandTotalOrderDetail.text = "Rp. ${transaksi.total?:"-"}"
        binding.textTanggalPemesananOrderDetail.text = "Tanggal : ${transaksi.createdAt?:"-"}"
        if (transaksi.status == 1 || transaksi.status == 2)
            binding.buttonBayarOrderDetail.visibility = android.view.View.VISIBLE
        if (transaksi.status == 4 || transaksi.status == 5) {
            binding.buttonCancelOrderDetail.visibility = android.view.View.GONE
            binding.buttonBayarOrderDetail.visibility = android.view.View.GONE
        }
        binding.layoutStatus.setBackgroundColor(statusColor(transaksi.status))
        setupRecyclerView()
    }

    private fun setObservers() {
        orderDetailViewModel=ViewModelProvider(this).get(OrderViewModel::class.java)
    }

    private fun setupRecyclerView() {
        galonAdapter = GalonOrderDetailAdapter(this, transaksi.detailTransaksi)
        binding.rvOrderDetail.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvOrderDetail.adapter = galonAdapter
    }
    private fun statusColor(status: Int): Int {
        return when(status){
            0 -> R.color.ocean_200
            1 -> android.graphics.Color.YELLOW
            2 -> android.graphics.Color.RED
            3 -> R.color.ocean_500
            4 -> android.graphics.Color.GREEN
            5 -> android.graphics.Color.RED
            else -> R.color.ocean_700
        }
    }
    private fun statusToString(status: Int): String {
        return when (status) {
            0 -> "belum dikonfirmasi"
            1 -> "belum dibayar"
            2 -> "pembayaran gagal"
            3 -> "belum dikirim"
            4 -> "selesai"
            5 -> "dibatalkan"
            else -> "error"
        }
    }


}
