package com.example.galonapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.config.Constant
import com.example.galonapps.databinding.GridLaporanBinding

import com.example.galonapps.model.DetailTransaksi
import com.example.galonapps.model.LaporanKurir
import com.squareup.picasso.Picasso
import timber.log.Timber

class LaporanKurirAdapter(private val context: Context, private val laporanKurirList: List<LaporanKurir>) :
    RecyclerView.Adapter<LaporanKurirAdapter.LaporanItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LaporanItemViewHolder {
        val binding: GridLaporanBinding = GridLaporanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaporanItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaporanItemViewHolder, position: Int) {
        holder.bind(laporanKurirList[position], holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return laporanKurirList.size
    }

    class LaporanItemViewHolder(var binding: GridLaporanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(laporanKurir: LaporanKurir, position: Int) {
            Picasso.Builder(itemView.context).build().load("${Constant.BASE_URL}${laporanKurir.icon}")
                .into(binding.imageView2)
            binding.textNamaLaporan.text = laporanKurir.nama ?: ""
            binding.textDataLaporan.text = "${laporanKurir.jumlah}" ?: ""
        }

        private fun itemsCount(detailTransaksi: MutableList<DetailTransaksi>): Int {
            return detailTransaksi.size
        }
    }
}
