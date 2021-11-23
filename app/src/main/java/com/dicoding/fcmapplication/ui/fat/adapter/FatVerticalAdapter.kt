package com.dicoding.fcmapplication.ui.fat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.visible

class FatVerticalAdapter : PagingRecyclerViewAdapter<FatVerticalAdapter.FatViewHolder, Fat>() {


    private var onClick: ((Fat) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FatVerticalAdapter.FatViewHolder
        get() = { inflater, viewGroup, boolean ->
            FatViewHolder(ItemDeviceLinearLayoutBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FatVerticalAdapter.FatViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FatViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<Fat, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: Fat) {
            with(binding) {
                tvDeviceName.text = data.fatName
                data.fatImage?.let { imageDevice.glide(itemView, it) }
                if (data.fatIsService == true){
                    imageIsService.visible()
                    tvTagCondition.text = "Need Service"
                }else{
                    imageIsService.invisible()
                    tvTagCondition.text = "Normal"
                }
                tvActiveDate.text = data.fatActivateDate

                root.setOnClickListener {
                    onClick?.invoke(data)
                }
            }
        }

    }

    fun setOnClickData(onClick: (data: Fat) -> Unit) {
        this.onClick = onClick
    }
}