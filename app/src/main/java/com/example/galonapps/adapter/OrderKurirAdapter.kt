package com.example.galonapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.databinding.ListOrderKurirBinding
import com.example.galonapps.model.Transaksi

class OrderKurirAdapter(
    private val context: Context,
    private val orderItemList: List<Transaksi>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<OrderKurirAdapter.OrderItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderItemViewHolder {
        val binding: ListOrderKurirBinding =
            ListOrderKurirBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(orderItemList[position], holder.adapterPosition, listener as OnItemClickListener)
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    class OrderItemViewHolder(var binding: ListOrderKurirBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Transaksi, position: Int, listener: OnItemClickListener) {
            binding.textNamaUserListOrderKurir.text = order.pelanggan?.nama ?: "-"
            binding.textDesaUserListOrderKurir.text = "Desa : ${order.pelanggan?.desa?.nama}"
            binding.textAlamatUserListOrderKurir.text = order.pelanggan?.alamat
            binding.textDateListOrderKurir.text = "Tanggal : ${order.createdAt}"
            binding.root.setOnClickListener { listener.onClickListener(position) }
            if (order.status != 3)
                binding.buttonDrive.visibility = ViewGroup.GONE
            binding.buttonDrive.setOnClickListener { listener.onDriverClickListener(position) }
        }

    }

    interface OnItemClickListener {
        fun onClickListener(position: Int)
        fun onDriverClickListener(position: Int)
    }
}
