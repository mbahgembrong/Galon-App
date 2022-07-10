package com.example.galonapps.ui.pelanggan.transaksi

import android.content.Intent
import android.os.Bundle
import android.telecom.Call.Callback
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.App
import com.example.galonapps.adapter.GalonTransaksiListAdapter
import com.example.galonapps.databinding.ActivityTransaksiBinding
import com.example.galonapps.model.CartItem
import com.example.galonapps.model.TransaksiRequest
import com.example.galonapps.prefs
import com.example.galonapps.ui.pelanggan.PelangganActivity

class TransaksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransaksiBinding
    private lateinit var transaksiAdapter: GalonTransaksiListAdapter
    private lateinit var transaksiViewModel: TransaksiViewModel
    private var cartList: ArrayList<CartItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setObservers()
        transaksiViewModel.getCart()
        transaksiViewModel.getHargaTotal()
        binding.buttonTransaksi.setOnClickListener {
            App.alertDialog(this) {
                transaksiViewModel.addTransaksi(
                    TransaksiRequest(
                        null,
                        prefs.idPelanggan,
                        null,
                        cartList,
                        null,
                        null
                    )
                )
                val intent = Intent(this, PelangganActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent).also { finish() }
            }

        }
        binding.buttonGantiAlamat.setOnClickListener {
            val intent = Intent(this, AlamatActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateView()
        setObservers()
    }

    private fun initView() {
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateView()
        setupOrderRecyclerView()
    }

    private fun updateView() {
        binding.textnamaUserTransaksi.text = prefs.nama
        binding.textAlamatUserTransaksi.text = prefs.alamat
    }

    fun setObservers() {
        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        transaksiViewModel.getCartDataList.observe(this) {
            if (it == null) {
                finish()
                Toast.makeText(this, "No result found", Toast.LENGTH_LONG).show()
            } else {
                cartList.clear()
                it.let { it1 -> cartList.addAll(it1) }
                transaksiAdapter.notifyDataSetChanged()
            }
        }
        transaksiViewModel.getHargaTotal.observe(this) {
            binding.textTotalHargaTransaksi.text = App.currencyFormat(it)
            binding.textOngkirTransaksi.text = App.currencyFormat(prefs.getDesa()?.ongkir)
            val grandTotal = it + (prefs.getDesa()?.ongkir ?: 0)
            binding.textGrandTotalTransaksi.text = App.currencyFormat(grandTotal)
            binding.textGrandTotalTransaksi2.text = App.currencyFormat(grandTotal)
            binding.buttonTransaksi.isEnabled = grandTotal > 0
        }
        transaksiViewModel.getTransaksi.observe(this) {
//            if (it != null) {
//                val intent = Intent(this, BayarActivity::class.java)
//                val bundle = Bundle()
//                bundle.putString("idTransaksi", it.id)
//                bundle.putInt("status", it.status)
//                bundle.putInt("grandTotal", it.total)
//                intent.putExtra(BayarActivity.ACTIVITY_PEMBAYARAN, bundle)
//                startActivity(intent).also { finish() }
//            }
        }
    }

    private fun setupOrderRecyclerView() {
        transaksiAdapter =
            GalonTransaksiListAdapter(this, cartList, object : GalonTransaksiListAdapter.OnItemClickListener {
                override fun onQuantityAdd(position: Int) {
                    transaksiViewModel.addQuantity(cartList[position])
                    transaksiAdapter.notifyDataSetChanged()
                }

                override fun onQuantitySub(position: Int) {
                    transaksiViewModel.subQuantity(cartList[position])
                    transaksiAdapter.notifyDataSetChanged()
                }
            })
        binding.rvTransaksi.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvTransaksi.adapter = transaksiAdapter

    }

}
