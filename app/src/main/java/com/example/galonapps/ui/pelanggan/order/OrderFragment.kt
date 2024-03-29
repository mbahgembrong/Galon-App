package com.example.galonapps.ui.pelanggan.order

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.galonapps.adapter.OrderAdapter
import com.example.galonapps.databinding.FragmentOrderBinding
import com.example.galonapps.model.Transaksi
import com.example.galonapps.ui.pelanggan.order.OrderDetailActivity.Companion.ORDER_DETAIL_ACTIVITY
import timber.log.Timber

class OrderFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var _binding: FragmentOrderBinding? = null
    private lateinit var orderAdapter: OrderAdapter
    private var orderList: ArrayList<Transaksi> = ArrayList()

    private val binding get() = _binding!!

    val status = arrayOf(
        "All",
        "belum konfirmasi",
        "belum dibayar",
        "pembayaran gagal",
        "belum dikirim",
        "selesai",
        "dibatalkan"
    )
    var statusSelected = "All"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initView()
        setObservers()
        return root
    }

    override fun onResume() {
        super.onResume()
        orderViewModel.getOrder()
    }

    fun initView() {
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, status)
        (binding.spinerStatus).apply {
            setAdapter(adapter)
        }
        binding.spinerStatus.onItemSelectedListener = object :
            android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                statusSelected = "All"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                statusSelected = status[position]
                if (statusSelected == "All") {
                    orderViewModel.getOrder()
                } else {
                    orderViewModel.getOrder(statusSelected)
                }
            }
        }
        setupRecyclerView()
    }

    fun setObservers() {
        orderViewModel =
            ViewModelProvider(this)[OrderViewModel::class.java]
        orderViewModel.getOrderDataList.observe(viewLifecycleOwner, Observer {
            if (it == null)
                Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show()
            else {
                orderList.clear()
                orderList.addAll(it)
                orderAdapter.notifyDataSetChanged()
            }
        })
    }

    fun setupRecyclerView() {
        orderAdapter = OrderAdapter(requireContext(), orderList, object : OrderAdapter.OnItemClickListener {
            override fun onClickListener(position: Int) {
//                Timber.d("detail ${orderList[position].detailTransaksi[0].id}")
                val intent = Intent(requireContext(), OrderDetailActivity::class.java)
                intent.putExtra(ORDER_DETAIL_ACTIVITY, orderList[position])
                startActivity(intent)
            }
        })
        binding.rvOrder.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvOrder.adapter = orderAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
