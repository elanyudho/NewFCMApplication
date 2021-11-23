package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.visible

class FdtVerticalAdapter : PagingRecyclerViewAdapter<FdtVerticalAdapter.FdtViewHolder, Fdt>() {


    private var onClick: ((Fdt) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FdtVerticalAdapter.FdtViewHolder
        get() = { inflater, viewGroup, boolean ->
            FdtViewHolder(ItemDeviceLinearLayoutBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FdtVerticalAdapter.FdtViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FdtViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<Fdt, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: Fdt) {
            with(binding) {
                tvDeviceName.text = data.fdtName
                data.fdtImage?.let { imageDevice.glide(itemView, it) }
                if (data.fdtIsService == true){
                    imageIsService.visible()
                    tvTagCondition.text = "Need Service"
                }else{
                    imageIsService.invisible()
                    tvTagCondition.text = "Normal"
                }
                tvActiveDate.text = data.fdtActivateDate

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