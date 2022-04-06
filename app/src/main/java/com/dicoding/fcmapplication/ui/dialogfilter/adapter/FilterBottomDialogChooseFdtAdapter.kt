package com.dicoding.fcmapplication.ui.dialogfilter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemFilterBottomDialogBinding
import com.dicoding.fcmapplication.domain.model.FdtToAdd

class FilterBottomDialogChooseFdtAdapter: BaseRecyclerViewAdapter<FilterBottomDialogChooseFdtAdapter.Holder>() {

    private var listData = mutableListOf<FdtToAdd>()
    private var onClick :((data: FdtToAdd) -> Unit)? = null

    fun submitList(newList : List<FdtToAdd>){
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
        BaseViewHolder<FdtToAdd, ItemFilterBottomDialogBinding>(itemView){
        override fun bind(data: FdtToAdd) {
            binding.tvArrayFilter.text = data.fdtName
            binding.root.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }

    fun setOnClickItemListener(onClick: (data: FdtToAdd)  -> Unit){
        this.onClick = onClick
    }
    override fun getItemCount(): Int {
        return listData.size
    }
}