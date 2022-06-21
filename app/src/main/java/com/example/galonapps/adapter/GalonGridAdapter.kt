package com.example.galonapps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galonapps.R
import com.example.galonapps.databinding.GridGalonBinding
import com.example.galonapps.model.Galon
import com.squareup.picasso.Picasso

class GalonGridAdapter(private val context: Context, private val galonItemList: List<Galon>, private val listener: OnItemClickListener) : RecyclerView.Adapter<GalonGridAdapter.GalonItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalonItemViewHolder {
        val binding: GridGalonBinding = GridGalonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalonItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalonItemViewHolder, position: Int) {
        holder.bind(galonItemList[position], holder.adapterPosition, listener as OnItemClickListener)
    }

    override fun getItemCount(): Int {
        return galonItemList.size
    }

    class GalonItemViewHolder(var binding: GridGalonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(galon: Galon, position: Int, listener: OnItemClickListener) {
            Picasso.get().load(galon.image).placeholder(R.drawable.ic_launcher_background).into(binding.imageGridGalon)
            binding.textMerekGrid.text = galon.merk
            binding.textIsiGrid.text =galon.isiGalon
            binding.textHargaGrid.text = "Rp. " + galon.hargaJual
            binding.buttonTambahGrid.setOnClickListener {
                it.visibility = View.GONE
                binding.layoutQuantityControl.apply {
                    root.visibility = View.VISIBLE
                    textQuantity.text = "1"
                }
            }
            binding.layoutQuantityControl.imageAdd.setOnClickListener {
                listener.onQuantityAdd(position)
                binding.layoutQuantityControl.textQuantity.apply {
                    text = ((text.toString().toInt()+1).toString())
                }
            }
            binding.layoutQuantityControl.imageSub.setOnClickListener {
                listener.onQuantitySub(position)
                if (binding.layoutQuantityControl.textQuantity.text.toString().toInt() == 1) {
                    binding.layoutQuantityControl.root.visibility = View.GONE
                    binding.buttonTambahGrid.visibility = View.VISIBLE
                }else
                binding.layoutQuantityControl.textQuantity.apply {
                    text = ((this.text.toString().toInt()-1).toString())
                }
            }
        }
    }


    interface OnItemClickListener {
        fun onQuantityAdd(position: Int)
        fun onQuantitySub(position: Int)
    }
}
