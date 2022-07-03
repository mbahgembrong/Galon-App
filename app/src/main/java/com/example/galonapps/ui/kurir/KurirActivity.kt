package com.example.galonapps.ui.kurir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.galonapps.R
import com.example.galonapps.adapter.OrderKurirAdapter
import com.example.galonapps.databinding.ActivityKurirBinding
import com.example.galonapps.model.Transaksi
import com.example.galonapps.ui.kurir.maps.MapsActivity
import com.example.galonapps.ui.pelanggan.order.OrderDetailActivity

class KurirActivity : AppCompatActivity() {
    lateinit var viewModel: KurirViewModel
    lateinit var binding: ActivityKurirBinding
    lateinit var orderKurirAdapter: OrderKurirAdapter
    private var orderKurirList: ArrayList<Transaksi> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setObserver()
        viewModel.getOrderKurir()
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(KurirViewModel::class.java)
        viewModel.getOrderKurirList.observe(this, androidx.lifecycle.Observer {
            orderKurirList.addAll(it)
            orderKurirAdapter.notifyDataSetChanged()
        })

    }

    private fun initView() {
        binding = ActivityKurirBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        orderKurirAdapter = OrderKurirAdapter(this, orderKurirList, object : OrderKurirAdapter.OnItemClickListener {
            override fun onClickListener(position: Int) {
                val intent = Intent(baseContext, OrderDetailActivity::class.java)
                intent.putExtra(OrderDetailActivity.ORDER_DETAIL_ACTIVITY, orderKurirList[position])
                startActivity(intent)
            }

            override fun onDriverClickListener(position: Int) {
                val intent = Intent(baseContext, MapsActivity::class.java)
                intent.putExtra(MapsActivity.MAPS_ACTIVITY_KEY, orderKurirList[position])
                startActivity(intent)
            }
        })
        binding.rvListOrderKurir.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvListOrderKurir.adapter = orderKurirAdapter

    }
}
