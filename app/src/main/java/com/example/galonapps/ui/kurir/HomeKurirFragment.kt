package com.example.galonapps.ui.kurir

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galonapps.R
import com.example.galonapps.adapter.LaporanKurirAdapter
import com.example.galonapps.adapter.OrderKurirAdapter
import com.example.galonapps.databinding.ActivityKurirBinding
import com.example.galonapps.databinding.FragmentHomeBinding
import com.example.galonapps.databinding.FragmentHomeKurirBinding
import com.example.galonapps.model.LaporanKurir
import com.example.galonapps.model.Transaksi
import com.example.galonapps.prefs
import com.example.galonapps.ui.kurir.maps.MapsActivity
import com.example.galonapps.ui.login.LoginActivity
import com.mapbox.geojson.Point
import mumayank.com.airlocationlibrary.AirLocation
import timber.log.Timber

class HomeKurirFragment : Fragment() {
    lateinit var viewModel: KurirViewModel
    lateinit var binding: FragmentHomeKurirBinding
    lateinit var orderKurirAdapter: OrderKurirAdapter
    private var orderKurirList: ArrayList<Transaksi> = ArrayList()
    lateinit var laporanKurirAdapter: LaporanKurirAdapter
    private var laporanKurirList: ArrayList<LaporanKurir> = ArrayList()
    var kurirPoint: Point? = null
    var airLoc: AirLocation? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeKurirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLocation()
        setObserver()
        binding.buttonLogOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah anda yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, which ->
                    dialog.dismiss()
                    viewModel.logout()
                    prefs.isLoggedIn = false
                    prefs.clearPreferences()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent).also { activity?.finish() }
                }
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(KurirViewModel::class.java)
        viewModel.getOrderKurirList.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                Timber.d("getOrderKurirList: $it")
                orderKurirList.clear()
                it.map { transaksi ->
                    euclidean(
                        transaksi.pelanggan?.lang!!.toDouble(),
                        transaksi.pelanggan?.long!!.toDouble(),
                        kurirPoint?.latitude()?.toDouble()!!,
                        kurirPoint?.longitude()?.toDouble()!!
                    ).let {
                        transaksi.jarak = it
                    }
                }
                orderKurirList.addAll(it.sortedBy { it.jarak })
                orderKurirAdapter.notifyDataSetChanged()
            }
        })
        viewModel.getOrderKurir()
        viewModel.getLaporanKurirList.observe(requireActivity(), Observer {
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
        binding.namaKurir.text = prefs.nama
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        orderKurirAdapter =
            OrderKurirAdapter(requireContext(), orderKurirList, object : OrderKurirAdapter.OnItemClickListener {
                override fun onClickListener(position: Int) {
                    val intent = Intent(requireContext(), DetailOrderKurirActivity::class.java)
                    intent.putExtra(DetailOrderKurirActivity.ORDER_DETAIL_ACTIVITY, orderKurirList[position])
                    startActivity(intent)
                }

                override fun onDriverClickListener(position: Int) {
                    val intent = Intent(requireContext(), MapsActivity::class.java)
                    intent.putExtra(MapsActivity.MAPS_ACTIVITY_KEY, orderKurirList[position])
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent).also { activity?.finish() }
                }
            })
        binding.rvListOrderKurir.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.rvListOrderKurir.adapter = orderKurirAdapter
        laporanKurirAdapter =
            LaporanKurirAdapter(requireContext(), laporanKurirList, object : LaporanKurirAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(requireContext(), LaporanActivity::class.java)
                    intent.putExtra(LaporanActivity.EXTRA_LAPORAN, laporanKurirList[position])
                    startActivity(intent)
                }
            })
        binding.rvLaporan.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvLaporan.adapter = laporanKurirAdapter

    }

    fun getLocation() {
        airLoc = AirLocation(requireActivity(), true, true,
            object : AirLocation.Callbacks {
                override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                    Toast.makeText(
                        requireContext(), "Gagal mendapatkan posisi saat ini",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess(location: Location) {
                    kurirPoint = Point.fromLngLat(location.longitude, location.latitude)
                }
            })
    }

    fun euclidean(
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double
    ): Double {
        val titik = (x2 - x1) * (x2 - x1) +
                (y2 - y1) * (y2 - y1)
        return Math.sqrt(titik)
    }
}
