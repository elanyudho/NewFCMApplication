package com.dicoding.fcmapplication.ui.fat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.visible

class SearchFatNoPaginationAdapter :
    BaseRecyclerViewAdapter<SearchFatNoPaginationAdapter.SearchFatHorizontalViewHolder>() {

    private var listData = mutableListOf<Fat>()

    lateinit var onClick: (data: Fat) -> Unit
    fun submitList(newList: List<Fat>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    inner class SearchFatHorizontalViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<Fat, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: Fat) {
            with(binding) {
                data.fatImage?.let { imageDevice.glide(itemView, it) }
                tvDeviceName.text = data.fatName
                tvActiveDate.text = data.fatActivateDate
                if(data.fatIsService == true) {
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

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> SearchFatNoPaginationAdapter.SearchFatHorizontalViewHolder
        get() = { inflater, viewGroup, boolean ->
            SearchFatHorizontalViewHolder(ItemDeviceLinearLayoutBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: SearchFatNoPaginationAdapter.SearchFatHorizontalViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setOnClickData(onClick: (data: Fat) -> Unit) {
        this.onClick = onClick
    }
}