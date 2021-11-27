package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.visible

class SearchFdtNoPaginationAdapter :
    BaseRecyclerViewAdapter<SearchFdtNoPaginationAdapter.SearchFdtHorizontalViewHolder>() {

    private var listData = mutableListOf<Fdt>()

    lateinit var onClick: (data: Fdt) -> Unit
    fun submitList(newList: List<Fdt>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    inner class SearchFdtHorizontalViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<Fdt, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: Fdt) {
            with(binding) {
                data.fdtImage?.let { imageDevice.glide(itemView, it) }
                tvDeviceName.text = data.fdtName
                tvActiveDate.text = data.fdtActivateDate
                if(data.fdtIsService == true) {
                    imageIsService.visible()
                    tvTagCondition.text = "Need Service"
                }else{
                    imageIsService.invisible()
                    tvTagCondition.text = "Normal"
                }

                binding.root.setOnClickListener {
                    onClick.invoke(data)
                }
            }
        }

    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> SearchFdtNoPaginationAdapter.SearchFdtHorizontalViewHolder
        get() = { inflater, viewGroup, boolean ->
            SearchFdtHorizontalViewHolder(ItemDeviceLinearLayoutBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: SearchFdtNoPaginationAdapter.SearchFdtHorizontalViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setOnClickData(onClick: (data: Fdt) -> Unit) {
        this.onClick = onClick
    }
}