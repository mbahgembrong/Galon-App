package com.example.galonapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.App
import com.example.galonapps.R
import com.example.galonapps.config.Constant
import com.example.galonapps.databinding.ListGalonTransaksiBinding
import com.example.galonapps.model.CartItem
import com.example.galonapps.ui.pelanggan.home.Cart
import com.squareup.picasso.Picasso

class GalonTransaksiListAdapter(
    private val context: Context,
    private val galonItemList: List<CartItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<GalonTransaksiListAdapter.GalonItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalonItemViewHolder {
        val binding: ListGalonTransaksiBinding =
            ListGalonTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalonItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalonItemViewHolder, position: Int) {
        holder.bind(galonItemList[position], holder.adapterPosition, listener as OnItemClickListener)
    }

    override fun getItemCount(): Int {
        return galonItemList.size
    }

    class GalonItemViewHolder(var binding: ListGalonTransaksiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: CartItem, position: Int, listener: OnItemClickListener) {
            Picasso.get().load("${Constant.GALON_URL}${cart.galon.image}")
                .placeholder(R.drawable.ic_water)
                .into(binding.imageGalonTransaksi)
            binding.textNamaGalonTransaksi.text = cart.galon.merk
            binding.textHargaGalonTransaksi.text = App.currencyFormat(cart.galon.hargaJual)
            binding.layoutQuantityControl.apply {
                root.visibility = View.VISIBLE
                textQuantity.text = Cart.showItem(cart.galon)!!.quantity.toString()
            }
            binding.layoutQuantityControl.imageAdd.setOnClickListener {
                listener.onQuantityAdd(position)
                binding.layoutQuantityControl.textQuantity.apply {
                    text = ((text.toString().toInt() + 1).toString())
                }
            }
            binding.layoutQuantityControl.imageSub.setOnClickListener {
                listener.onQuantitySub(position)
                if (binding.layoutQuantityControl.textQuantity.text.toString().toInt() == 1) {
                    binding.layoutQuantityControl.root.visibility = View.GONE
                } else
                    binding.layoutQuantityControl.textQuantity.apply {
                        text = ((this.text.toString().toInt() - 1).toString())
                    }
            }
        }
    }


    interface OnItemClickListener {
        fun onQuantityAdd(position: Int)
        fun onQuantitySub(position: Int)
    }
}
