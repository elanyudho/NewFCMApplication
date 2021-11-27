package com.dicoding.fcmapplication.ui.fat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemRectangleBinding
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.utils.extensions.glide

class FatGridAdapter : PagingRecyclerViewAdapter<FatGridAdapter.FatViewHolder, Fat>() {


    private var onClick: ((Fat) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FatViewHolder
        get() = { inflater, viewGroup, boolean ->
            FatViewHolder(ItemRectangleBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FatViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FatViewHolder(itemView: ItemRectangleBinding) :
        BaseViewHolder<Fat, ItemRectangleBinding>(itemView) {
        override fun bind(data: Fat) {
            with(binding) {
                tvName.text = data.fatName
                data.fatImage?.let { ivImage.glide(itemView, it) }

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