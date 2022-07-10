package com.example.galonapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.App
import com.example.galonapps.R
import com.example.galonapps.databinding.ListGalonOrderBinding
import com.example.galonapps.model.DetailTransaksi
import com.example.galonapps.model.Transaksi
import com.squareup.picasso.Picasso

class OrderAdapter(
    private val context: Context,
    private val orderItemList: List<Transaksi>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderItemViewHolder {
        val binding: ListGalonOrderBinding =
            ListGalonOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            binding.textGrandTotalGalonOrder.text = "Grand Total : ${App.currencyFormat(order.total)}"
            binding.textStatusGalonOrder.text = "Status : ${statusToString(order.status)}"
            binding.textItemGalonOrder.text = " ${itemsCount(order.detailTransaksi as ArrayList)} items"
            binding.textTanggalGalonOrder.text = "Tanggal : ${order.createdAt}"
            binding.root.setOnClickListener { listener.onClickListener(position) }
            binding.textStatusGalonOrder.setTextColor(statusColor(order.status))

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

    }


    interface OnItemClickListener {
        fun onClickListener(position: Int)
    }
}
