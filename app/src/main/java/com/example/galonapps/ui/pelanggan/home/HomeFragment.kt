package com.example.galonapps.ui.pelanggan.home

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.adapter.GalonGridAdapter
import com.example.galonapps.databinding.ActivityPelangganBinding
import com.example.galonapps.databinding.FragmentHomeBinding
import com.example.galonapps.model.CartItem
import com.example.galonapps.model.Galon
import com.example.galonapps.prefs
import com.example.galonapps.ui.component.VerticalMarginItemDecoration
import com.example.galonapps.ui.pelanggan.PelangganActivity


class HomeFragment : Fragment() {


    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var galonAdapter: GalonGridAdapter
    private var galonList: ArrayList<Galon> = ArrayList()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initView()
        return root
    }

    override fun onResume() {
        super.onResume()
        updateCartUI()
        setObservers()
        homeViewModel.getGalon()
    }

    fun initView(){
        binding.textNamaPelanggan.text = "Bayu Okta"

        setupGalonRecyclerView()
    }
    fun setObservers(){
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getGalonDataList.observe(this, Observer<List<Galon>> {
            if (it == null) {
                Toast.makeText(requireContext(), "No result found", Toast.LENGTH_LONG).show()
            } else {
                galonList.clear()
                it.let { it1 -> galonList.addAll(it1) }
                galonAdapter.notifyDataSetChanged()
            }
        })

    }
    fun setupGalonRecyclerView(){
        galonAdapter = GalonGridAdapter(requireContext(), galonList, object : GalonGridAdapter.OnItemClickListener {
            override fun onQuantityAdd(position: Int) {
                println("quantity add clicked $position")
                Cart.addItem(CartItem(galonList[position]))
                updateCartUI()
            }

            override fun onQuantitySub(position: Int) {
                println("quantity sub clicked $position")
                Cart.removeItem(CartItem(galonList[position]))
                Log.d("cart", prefs.getCart().toString())
                updateCartUI()
            }
        })
        binding.rvHome.layoutManager = GridLayoutManager(activity,2)
        binding.rvHome.adapter = galonAdapter
    }
    private fun updateCartUI() {
        if (!prefs.getCart().isNullOrEmpty()){
            (requireActivity() as PelangganActivity).bind?.apply {
                buttonCartPelanggan.visibility = View.VISIBLE
                textItemCart.text = Cart.getShoppingCartSize().toString()+" items"
                textGrandTotalHarga.text = Cart.getShoppingTotal().toString()
            }
        }
        else{
            (requireActivity() as PelangganActivity).bind?.apply {
                buttonCartPelanggan.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as PelangganActivity).bind?.apply {
            buttonCartPelanggan.visibility = View.GONE
        }
    }
}
