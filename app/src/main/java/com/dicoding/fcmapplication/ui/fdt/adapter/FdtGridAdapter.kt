package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemRectangleBinding
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.extensions.glide

class FdtGridAdapter : PagingRecyclerViewAdapter<FdtGridAdapter.FdtViewHolder, Fdt>() {


    private var onClick: ((Fdt) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FdtGridAdapter.FdtViewHolder
        get() = { inflater, viewGroup, boolean ->
            FdtViewHolder(ItemRectangleBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FdtGridAdapter.FdtViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FdtViewHolder(itemView: ItemRectangleBinding) :
        BaseViewHolder<Fdt, ItemRectangleBinding>(itemView) {
        override fun bind(data: Fdt) {
            with(binding) {
                tvName.text = data.fdtName
                data.fdtImage?.let { ivImage.glide(itemView, it) }

                root.setOnClickListener {
                    onClick?.invoke(data)
                }
            }
        }

    }

    fun setOnClickData(onClick: (data: Fdt) -> Unit) {
        this.onClick = onClick
    }
}