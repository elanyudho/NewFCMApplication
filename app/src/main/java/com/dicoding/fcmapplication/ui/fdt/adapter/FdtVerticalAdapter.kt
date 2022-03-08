package com.dicoding.fcmapplication.ui.fdt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.setTint
import com.dicoding.fcmapplication.utils.extensions.visible

class FdtVerticalAdapter : PagingRecyclerViewAdapter<FdtVerticalAdapter.FdtViewHolder, Fdt>() {


    private var onClick: ((Fdt) -> Unit)? = null

    private var valueIndicator = 0

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> FdtVerticalAdapter.FdtViewHolder
        get() = { inflater, viewGroup, boolean ->
            FdtViewHolder(ItemDeviceLinearLayoutBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: FdtVerticalAdapter.FdtViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class FdtViewHolder(itemView: ItemDeviceLinearLayoutBinding) :
        BaseViewHolder<Fdt, ItemDeviceLinearLayoutBinding>(itemView) {
        override fun bind(data: Fdt) {
            with(binding) {
                tvDeviceName.text = data.fdtName
                valueIndicator = setIndicatorValue(data.fdtCore!!, data.fdtCoreUsed!!)
                if (data.fdtIsService == true) {
                    imageIsService.visible()
                    tvTagCondition.setText(R.string.need_service)
                } else {
                    imageIsService.invisible()
                    tvTagCondition.setText(R.string.normal)
                }
                tvActiveDate.text = data.fdtActivated


                imgCapacityIndicator.setTint(
                    when{

                        valueIndicator <= 50 -> R.color.green_lime

                        valueIndicator in 51..75 -> R.color.yellow_tangerine

                        valueIndicator > 75 -> R.color.red_orange

                        else -> R.color.white
                    }
                )

                root.setOnClickListener {
                    onClick?.invoke(data)

                }
            }

        }
    }

    fun setOnClickData(onClick: (data: Fdt) -> Unit) {
        this.onClick = onClick
    }

    private fun setIndicatorValue(total: String, used: String): Int {
        val totalCoreInDouble = used.toDouble() / total.toDouble() * 100
        return totalCoreInDouble.toInt()
    }
}