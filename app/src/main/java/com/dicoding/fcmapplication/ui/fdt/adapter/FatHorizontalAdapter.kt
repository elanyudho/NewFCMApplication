package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemRectangleBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.utils.extensions.glide

class FatHorizontalAdapter :
    BaseRecyclerViewAdapter<FatHorizontalAdapter.FatHorizontalViewHolder>() {

    private var listData = mutableListOf<FdtDetail.FatList>()

    lateinit var onClick: (data: FdtDetail.FatList) -> Unit
    fun submitList(newList: List<FdtDetail.FatList>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    inner class FatHorizontalViewHolder(itemView: ItemRectangleBinding) :
        BaseViewHolder<FdtDetail.FatList, ItemRectangleBinding>(itemView) {
        override fun bind(data: FdtDetail.FatList) {
            with(binding) {
                data.fatImage?.let { ivImage.glide(itemView, it) }
                tvName.text = data.fatName

                binding.root.setOnClickListener {
                    onClick.invoke(data)
                }
            }
        }

    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FatHorizontalAdapter.FatHorizontalViewHolder
        get() = { inflater, viewGroup, boolean ->
            FatHorizontalViewHolder(ItemRectangleBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FatHorizontalAdapter.FatHorizontalViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setOnClickData(onClick: (data: FdtDetail.FatList) -> Unit) {
        this.onClick = onClick
    }
}