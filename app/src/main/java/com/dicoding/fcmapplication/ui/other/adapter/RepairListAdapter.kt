package com.dicoding.fcmapplication.ui.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemServiceListBinding
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.utils.extensions.glide

class RepairListAdapter : PagingRecyclerViewAdapter<RepairListAdapter.RepairListViewHolder, Repair>() {


    private var onClick: ((Repair) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> RepairListAdapter.RepairListViewHolder
        get() = { inflater, viewGroup, boolean ->
            RepairListViewHolder(ItemServiceListBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: RepairListAdapter.RepairListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class RepairListViewHolder(itemView: ItemServiceListBinding) :
        BaseViewHolder<Repair, ItemServiceListBinding>(itemView) {
        override fun bind(data: Repair) {
            with(binding) {
                tvDeviceName.text = data.deviceName
                data.deviceImage?.let { imageDevice.glide(itemView, it) }
                tvTagCondition.text = "Need Service"
                tvDeviceNote.text = data.deviceNote

                root.setOnClickListener {
                    onClick?.invoke(data)
                }
            }
        }

    }

    fun setOnClickData(onClick: (data: Repair) -> Unit) {
        this.onClick = onClick
    }
}