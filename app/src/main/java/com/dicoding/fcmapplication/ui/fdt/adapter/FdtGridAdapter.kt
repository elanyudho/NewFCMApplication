package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemFdtBinding
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.extensions.glide

class FdtGridAdapter : PagingRecyclerViewAdapter<FdtGridAdapter.FdtViewHolder, Fdt>(){


    private var onClick : ((Fdt) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FdtGridAdapter.FdtViewHolder
        get() = { inflater, viewGroup, boolean ->
            FdtViewHolder(ItemFdtBinding.inflate(inflater,viewGroup,boolean))
        }

    override fun onBindViewHolder(holder: FdtGridAdapter.FdtViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FdtViewHolder(itemView: ItemFdtBinding): BaseViewHolder<Fdt, ItemFdtBinding>(itemView){
        override fun bind(data: Fdt) {
            with(binding) {
                ivImageFdt.glide(itemView, data.fdtImage)
                tvFdtName.text = data.fdtName

                root.setOnClickListener {
                    onClick?.invoke(data)
                }
            }
        }

    }

    fun setOnClickData(onClick: (data: Fdt) -> Unit){
        this.onClick = onClick
    }
}