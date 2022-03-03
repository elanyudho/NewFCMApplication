package com.dicoding.fcmapplication.ui.fat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.setTint
import com.dicoding.fcmapplication.utils.extensions.visible

class FatVerticalAdapter : PagingRecyclerViewAdapter<FatVerticalAdapter.FatViewHolder, Fat>() {


    private var onClick: ((Fat) -> Unit)? = null

    private var valueIndicator = 0

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FatVerticalAdapter.FatViewHolder
        get() = { inflater, viewGroup, boolean ->
            FatViewHolder(ItemDeviceLinearLayoutBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FatVerticalAdapter.FatViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FatViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<Fat, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: Fat) {
            with(binding) {
                tvDeviceName.text = data.fatName
                valueIndicator = setIndicatorValue(data.fatCore!!, data.fatCoreUsed!!)
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

                root.setOnClickListener {
                    onClick?.invoke(data)

                }
            }
        }

    }

    fun setOnClickData(onClick: (data: Fat) -> Unit) {
        this.onClick = onClick
    }

    private fun setIndicatorValue(total: String, used: String): Int {
        val totalCoreInDouble = used.toDouble() / total.toDouble() * 100
        return totalCoreInDouble.toInt()
    }
}