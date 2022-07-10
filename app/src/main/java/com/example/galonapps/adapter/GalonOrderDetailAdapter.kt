package com.example.galonapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.App
import com.example.galonapps.config.Constant
import com.example.galonapps.databinding.ListGalonOrderDetailBinding
import com.example.galonapps.model.DetailTransaksi
import com.squareup.picasso.Picasso

class GalonOrderDetailAdapter(private val context: Context, private val orderDetailItemList: List<DetailTransaksi>) :
    RecyclerView.Adapter<GalonOrderDetailAdapter.OrderItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderItemViewHolder {
        val binding: ListGalonOrderDetailBinding =
            ListGalonOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(orderDetailItemList[position], holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return orderDetailItemList.size
    }

    class OrderItemViewHolder(var binding: ListGalonOrderDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderDetail: DetailTransaksi, position: Int) {
            Picasso.Builder(itemView.context).build().load("${Constant.GALON_URL}${orderDetail.galon?.image}")
                .into(binding.imageOrderDetail)
            binding.textNamaGalonOrderDetail.text = orderDetail.galon?.merk ?: ""
            binding.textHargaGalonOrderDetail.text = App.currencyFormat(orderDetail.galon?.hargaJual)
            binding.textItemGalonOrderDetail.text = "x ${orderDetail.jumlah}"
            binding.textTotalGalonOrderDetail.text = "Total : ${App.currencyFormat(orderDetail.totalHarga)}"
        }

        private fun itemsCount(detailTransaksi: MutableList<DetailTransaksi>): Int {
            return detailTransaksi.size
        }


    }
}
