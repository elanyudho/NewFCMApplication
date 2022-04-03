package com.dicoding.fcmapplication.ui.dialogfilter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemFilterBottomDialogBinding

class FilterBottomDialogAdapter: BaseRecyclerViewAdapter<FilterBottomDialogAdapter.Holder>() {

    private var listData = mutableListOf<String>()
    private var onClick :((data: String) -> Unit)? = null

    fun submitList(newList : List<String>){
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

    inner class Holder (itemView:ItemFilterBottomDialogBinding) :
        BaseViewHolder<String, ItemFilterBottomDialogBinding>(itemView){
        override fun bind(data: String) {
            binding.tvArrayFilter.text = data
            binding.root.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }

    fun setOnClickItemListener(onClick: (data: String)  -> Unit){
        this.onClick = onClick
    }
    override fun getItemCount(): Int {
        return listData.size
    }
}