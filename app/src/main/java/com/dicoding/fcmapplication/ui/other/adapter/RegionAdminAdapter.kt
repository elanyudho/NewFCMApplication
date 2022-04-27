package com.dicoding.fcmapplication.ui.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemRegionAdminBinding
import com.dicoding.fcmapplication.domain.model.RegionAdmin
import com.dicoding.fcmapplication.utils.extensions.glide

class RegionAdminAdapter : PagingRecyclerViewAdapter<RegionAdminAdapter.RegionAdminViewHolder, RegionAdmin>() {

    private var onClick: ((RegionAdmin) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> RegionAdminAdapter.RegionAdminViewHolder
        get() = { inflater, viewGroup, boolean ->
            RegionAdminViewHolder(ItemRegionAdminBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: RegionAdminAdapter.RegionAdminViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class RegionAdminViewHolder(itemView: ItemRegionAdminBinding) :
        BaseViewHolder<RegionAdmin, ItemRegionAdminBinding>(itemView) {
        override fun bind(data: RegionAdmin) {
            with(binding) {
                tvAdminName.text = data.adminName
                tvRegion.text = data.adminRegion + "Region"
                tvAdminPosition.text = data.adminPosition
                tvAdminEmail.text = data.adminEmail
                tvAdminPhone.text = data.adminPhone

                imgRegionAdmin.glide(itemView, data.adminImage)

                root.setOnClickListener {
                    onClick?.invoke(data)

                }
            }

        }
    }

    fun setOnClickData(onClick: (data: RegionAdmin) -> Unit) {
        this.onClick = onClick
    }

}
