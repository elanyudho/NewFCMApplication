package com.dicoding.fcmapplication.ui.dialogfilter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemFilterBottomDialogBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.model.FdtToAdd

class FilterBottomDialogCoveredFatAdapter: BaseRecyclerViewAdapter<FilterBottomDialogCoveredFatAdapter.Holder>() {

    private var listData = mutableListOf<FdtDetail.FatList>()
    private var onClick :((data: FdtDetail.FatList) -> Unit)? = null

    fun submitList(newList : List<FdtDetail.FatList>){
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> Holder
        get() = {layoutInflater, viewGroup, b ->
            Holder(ItemFilterBottomDialogBinding.inflate(layoutInflater,viewGroup,b))
        }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data = listData[position])
    }

    inner class Holder (itemView: ItemFilterBottomDialogBinding) :
        BaseViewHolder<FdtDetail.FatList, ItemFilterBottomDialogBinding>(itemView){
        override fun bind(data: FdtDetail.FatList) {
            binding.tvArrayFilter.text = data.fatName
            binding.root.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }

    fun setOnClickItemListener(onClick: (data: FdtDetail.FatList)  -> Unit){
        this.onClick = onClick
    }
    override fun getItemCount(): Int {
        return listData.size
    }
}