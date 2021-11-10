package com.dicoding.fcmapplication.ui.fat.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.databinding.ItemFdtBinding
import com.dicoding.fcmapplication.domain.model.Fat

class FatGridAdapter : PagingRecyclerViewAdapter<FatGridAdapter.FatViewHolder, Fat>(){


    private var onClick : ((Fat) -> Unit)? = null

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FatViewHolder
        get() = { inflater, viewGroup, boolean ->
            FatViewHolder(ItemFdtBinding.inflate(inflater,viewGroup,boolean))
        }

    override fun onBindViewHolder(holder: FatViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FatViewHolder(itemView: ItemFdtBinding): BaseViewHolder<Fat, ItemFdtBinding>(itemView){
        override fun bind(data: Fat) {
            with(binding) {
                ivImageFdt.glide(itemView, data.fatImage)
                tvFdtName.text = data.fatName

                root.setOnClickListener {
                    onClick?.invoke(data)
                }
            }
        }

    }

    fun setOnClickData(onClick: (data: Fat) -> Unit){
        this.onClick = onClick
    }
}