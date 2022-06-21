package com.example.galonapps.ui.pelanggan.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.R
import com.example.galonapps.adapter.GalonGridAdapter
import com.example.galonapps.databinding.FragmentHomeBinding
import com.example.galonapps.model.Galon
import com.example.galonapps.ui.component.VerticalMarginItemDecoration
import com.google.gson.Gson
import com.google.gson.GsonBuilder


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
        setObservers()
        homeViewModel.getGalon()
        return root
    }
    fun initView(){
        binding.textNamaPelanggan.text = "Bayu Okta"
        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener(){

        })
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
//                galonList[position].stok = galonList[position].stok + 1
//                galonAdapter.notifyItemChanged(position)
//                updateCartUI()
//                saveCart(galonList)
            }

            override fun onQuantitySub(position: Int) {
                println("quantity sub clicked $position")
//                if (galonList[position].stok - 1 >= 0) {
//                    galonList[position].stok = galonList[position].stok - 1
//                    if (galonList[position].stok == 0) {
//                        galonList.removeAt(position)
//                        galonAdapter.notifyDataSetChanged()
//                    } else {
//                        galonAdapter.notifyDataSetChanged()
//                    }
//                    updateCartUI()
//                    saveCart(galonList)
//                }
            }
        })
        binding.rvHome.layoutManager = GridLayoutManager(activity,2)
        binding.rvHome.addItemDecoration(VerticalMarginItemDecoration(0,0,200))
        binding.rvHome.adapter = galonAdapter
    }
    fun saveCart(galonItems: List<Galon>?) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val cartString = gson.toJson(galonItems)
//        preferencesHelper.cart = cartString
    }
    @SuppressLint("SetTextI18n")
    private fun updateCartUI() {
        var total = 0
        var totalItems = 0
//        if (cartList.size > 0) {
//
//            binding.layoutContent.visibility = View.VISIBLE
//            binding.layoutEmpty.visibility = View.GONE
//            for (i in cartList.indices) {
//                total += cartList[i].price * cartList[i].quantity
//                totalItems += 1
//            }
//            if(!isPickup) {
//                total += deliveryPrice.toInt()
//                preferencesHelper.cartDeliveryPref = "delivery"
//            }
//            binding.textTotal.text = "₹$total"
//            if (totalItems == 1) {
//                snackBar.setText("₹$total | $totalItems item")
//            } else {
//                snackBar.setText("₹$total | $totalItems items")
//            }
//            snackBar.show()
//            cartTotalPrice = total
//        } else {
//            preferencesHelper.clearCartPreferences()
//            snackBar.dismiss()
//            binding.layoutContent.visibility = View.GONE
//            binding.layoutEmpty.visibility = View.VISIBLE
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
