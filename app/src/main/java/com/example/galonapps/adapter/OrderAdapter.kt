package com.example.galonapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.R
import com.example.galonapps.databinding.ListGalonOrderBinding
import com.example.galonapps.model.DetailTransaksi
import com.example.galonapps.model.Transaksi
import com.squareup.picasso.Picasso

class OrderAdapter(private val context: Context, private val orderItemList: List<Transaksi>, private val listener: OnItemClickListener) : RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderItemViewHolder {
        val binding: ListGalonOrderBinding = ListGalonOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(orderItemList[position], holder.adapterPosition, listener as OnItemClickListener)
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    class OrderItemViewHolder(var binding: ListGalonOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Transaksi, position: Int, listener: OnItemClickListener) {
            binding.textGrandTotalGalonOrder.text = "Grand Total : Rp. ${order.total}"
            binding.textStatusGalonOrder.text = "Status : ${statusToString(order.status)}"
            binding.textItemGalonOrder.text = " ${itemsCount(order.detailTransaksi)} items"
            binding.textTanggalGalonOrder.text = "Tanggal : ${order.createdAt}"
            binding.root.setOnClickListener { listener.onClickListener(position) }

        }

        private fun itemsCount(detailTransaksi: MutableList<DetailTransaksi>): Int {
            return detailTransaksi.size
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
    interface OnItemClickListener {
        fun onClickListener(position: Int)
    }
}
