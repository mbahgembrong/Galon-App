package com.example.galonapps.ui.pelanggan.order

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.App
import com.example.galonapps.R
import com.example.galonapps.adapter.GalonOrderDetailAdapter
import com.example.galonapps.databinding.ActivityOrderDetailBinding
import com.example.galonapps.model.Transaksi
import com.example.galonapps.prefs
import com.example.galonapps.ui.pelanggan.transaksi.BayarActivity
import timber.log.Timber
import www.sanju.motiontoast.MotionToast

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var galonAdapter: GalonOrderDetailAdapter
    private lateinit var transaksi: Transaksi
    private lateinit var orderDetailViewModel: OrderViewModel

    companion object {
        const val ORDER_DETAIL_ACTIVITY = "order_detail_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaksi = intent.getParcelableExtra(ORDER_DETAIL_ACTIVITY)!!

        initView()
        setObservers()
        binding.buttonCancelOrderDetail.setOnClickListener {
            App.alertDialog(this) {
                orderDetailViewModel.cancelOrder(transaksi.id)
            }
        }
        binding.buttonBayarOrderDetail.setOnClickListener {
            val intent = Intent(this, BayarActivity::class.java)
            var bundle = Bundle()
            bundle.putString("idTransaksi", transaksi.id)
            bundle.putInt("status", transaksi.status)
            bundle.putInt("grandTotal", transaksi.total)
            intent.putExtra(BayarActivity.ACTIVITY_PEMBAYARAN, bundle)
            startActivity(intent)
            finish()
        }
    }


    private fun initView() {
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textStatusOrderDetail.text = "Pesanan ${statusToString(transaksi.status)}"
        binding.textKeteranganOrderDetail.text = transaksi.keterangan ?: ""
        binding.textKaryawanOrderDetail.text = "Karyawan : ${transaksi.karyawan?.nama ?: "-"}"
        binding.textKurirOrderDetail.text = "Kurir : ${transaksi.kurir?.nama ?: "-"}"
        binding.textNamaPelangganOrderDetail.text = "Pelanggan : ${transaksi.pelanggan?.nama ?: "-"}"
        binding.textAlamatOrderDetailPelanggan.text = " ${transaksi.alamat ?: "-"}"
        binding.textGrandTotalOrderDetail.text = App.currencyFormat(transaksi.total)
        binding.textTanggalPemesananOrderDetail.text = "Tanggal : ${transaksi.createdAt ?: "-"}"
        if (transaksi.status == 1 || transaksi.status == 2 || transaksi.buktiTransaksi == null)
            binding.buttonBayarOrderDetail.visibility = android.view.View.VISIBLE
        if (transaksi.status == 4 || transaksi.status == 5) {
            binding.buttonCancelOrderDetail.visibility = android.view.View.GONE
            binding.buttonBayarOrderDetail.visibility = android.view.View.GONE
        }
        binding.layoutStatus.setBackgroundColor(statusColor(transaksi.status))
        if (transaksi.ongkir != null) {
            binding.layoutOngkir.visibility = android.view.View.VISIBLE
            binding.textOngkirOrderDetail.text = App.currencyFormat(transaksi.ongkir)
        }
        if (transaksi.diskon != null && transaksi.diskon!! > 0) {
            binding.layoutDiskon.visibility = android.view.View.VISIBLE
            binding.textDiskonOrderDetail.text = App.currencyFormat(transaksi.diskon)
        }
        setupRecyclerView()
    }

    private fun setObservers() {
        orderDetailViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderDetailViewModel.isUpdateTransaksi.observe(this) {
            if (it) {
                toast("Sukses Membatalkan Pesanan", MotionToast.TOAST_SUCCESS)
                finish()
            } else {
                toast("Gagal embatalkan Pesanan", MotionToast.TOAST_ERROR)
            }
        }
    }

    private fun setupRecyclerView() {
        galonAdapter = GalonOrderDetailAdapter(this, transaksi.detailTransaksi)
        binding.rvOrderDetail.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvOrderDetail.adapter = galonAdapter
    }

    private fun statusColor(status: Int): Int {
        return when (status) {
            0 -> Color.parseColor("#66cee7")
            1 -> Color.parseColor("#fdb129")
            2 -> Color.parseColor("#9e2826")
            3 -> Color.parseColor("#006881")
            4 -> Color.parseColor("#269e30")
            5 -> Color.parseColor("#9e2826")
            else -> Color.parseColor("#9e2826")
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

    fun toast(message: String, status: String) {
        MotionToast.createToast(
            this, message,
            status,
            MotionToast.GRAVITY_TOP,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular)
        )
    }
}
