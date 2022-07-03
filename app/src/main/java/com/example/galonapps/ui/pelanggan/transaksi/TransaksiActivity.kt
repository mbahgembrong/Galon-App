package com.example.galonapps.ui.pelanggan.transaksi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.R
import com.example.galonapps.adapter.GalonTransaksiListAdapter
import com.example.galonapps.databinding.ActivityTransaksiBinding
import com.example.galonapps.model.CartItem
import com.example.galonapps.prefs

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
        transaksiViewModel.getGrandTotal()
        binding.buttonTransaksi.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Apakah dibayar sekarang?")
                .setPositiveButton("Ya") { dialog, which ->
                    transaksiViewModel.bayar()
                    finish()
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    transaksiViewModel.addTransaksi()
                    finish()
                }
                .show()
        }
        binding.buttonGantiAlamat.setOnClickListener {
            val intent = Intent(this, AlamatActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initView() {
        binding = ActivityTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textnamaUserTransaksi.text = prefs.nama
        binding.textAlamatUserTransaksi.text = prefs.alamat
        setupOrderRecyclerView()
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
        transaksiViewModel.getGrandTotal.observe(this) {
            binding.textGrandTotalTransaksi2.text = "Rp. $it"
            binding.buttonTransaksi.isEnabled = it != 0
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
