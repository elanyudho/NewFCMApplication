package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.databinding.ItemRectangleBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.setTint
import com.dicoding.fcmapplication.utils.extensions.visible

class MoreFatAdapter :
    BaseRecyclerViewAdapter<MoreFatAdapter.FatHorizontalViewHolder>() {

    private var listData = mutableListOf<FdtDetail.FatList>()

    var valueIndicator = 0

    lateinit var onClick: (data: FdtDetail.FatList) -> Unit
    fun submitList(newList: List<FdtDetail.FatList>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    inner class FatHorizontalViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<FdtDetail.FatList, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: FdtDetail.FatList) {
            with(binding) {

                tvDeviceName.text = data.fatName
                if (data.fatIsService == true) {
                    imageIsService.visible()
                    tvTagCondition.setText(R.string.need_service)
                } else {
                    imageIsService.invisible()
                    tvTagCondition.setText(R.string.normal)
                }
                tvActiveDate.text = data.fatActivated

                if (valueIndicator <= 50) {
                    imgCapacityIndicator.setTint(R.color.green_lime)
                }
                if (valueIndicator in 51..75) {
                    imgCapacityIndicator.setTint(R.color.yellow_tangerine)
                }
                if (valueIndicator > 75) {
                    imgCapacityIndicator.setTint(R.color.red_orange)
                }

                binding.root.setOnClickListener {
                    onClick.invoke(data)
                }
            }
        }

    }

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> MoreFatAdapter.FatHorizontalViewHolder
        get() = { inflater, viewGroup, boolean ->
            FatHorizontalViewHolder(
                ItemDeviceLinearLayoutBinding.inflate(
                    inflater,
                    viewGroup,
                    boolean
                )
            )
        }

    override fun onBindViewHolder(holder: MoreFatAdapter.FatHorizontalViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    fun setOnClickData(onClick: (data: FdtDetail.FatList) -> Unit) {
        this.onClick = onClick
    }
}