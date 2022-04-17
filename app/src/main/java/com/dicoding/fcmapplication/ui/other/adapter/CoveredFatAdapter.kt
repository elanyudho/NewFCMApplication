package com.dicoding.fcmapplication.ui.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.databinding.ItemFatSelectBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.model.Repair

class CoveredFatAdapter: RecyclerView.Adapter<CoveredFatAdapter.Holder>() {

    private var listData = ArrayList<FdtDetail.FatList>()
    private var onClickDelete: ((data: FdtDetail.FatList) -> Unit?)? = null

    fun submitList(newList: List<FdtDetail.FatList>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoveredFatAdapter.Holder {
        return Holder(ItemFatSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: CoveredFatAdapter.Holder,
        position: Int
    ) {
        holder.bind(data = listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder (itemView: ItemFatSelectBinding) :
        BaseViewHolder<FdtDetail.FatList, ItemFatSelectBinding>(itemView){
        override fun bind(data: FdtDetail.FatList) {
            binding.tvFat.text = data.fatName
            binding.root.setOnClickListener {
                onClickDelete?.invoke(data)
            }
        }
    }

    /*fun setData(newListData: List<FdtDetail.FatList>) {
        val diffUtil = DiffUtilCoveredFatClass(oldListData, newListData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldListData.clear()
        oldListData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }*/

    fun setOnDeleteSkill(onClickDelete : (data:FdtDetail.FatList) -> Unit){
        this.onClickDelete = onClickDelete
    }


}