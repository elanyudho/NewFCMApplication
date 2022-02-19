package com.dicoding.fcmapplication.ui.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ItemServiceListBinding
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.setTint
import com.dicoding.fcmapplication.utils.extensions.visible

class RepairListAdapter : PagingRecyclerViewAdapter<RepairListAdapter.RepairListViewHolder, Repair>() {


    private var onClick: ((Repair) -> Unit)? = null

    var valueIndicator = 0

    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> RepairListAdapter.RepairListViewHolder
        get() = { inflater, viewGroup, boolean ->
            RepairListViewHolder(ItemServiceListBinding.inflate(inflater, viewGroup, boolean))
        }

    override fun onBindViewHolder(holder: RepairListAdapter.RepairListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    inner class RepairListViewHolder(itemView: ItemServiceListBinding) :
        BaseViewHolder<Repair, ItemServiceListBinding>(itemView) {
        override fun bind(data: Repair) {
            with(binding) {
                tvDeviceName.text = data.deviceName
                if (data.deviceIsService == true) {
                    imageIsService.visible()
                    tvTagCondition.setText(R.string.need_service)
                } else {
                    imageIsService.invisible()
                    tvTagCondition.setText(R.string.normal)
                }
                tvRepairNotes.text = data.deviceNote

                if (valueIndicator <= 50) {
                    imgCapacityIndicator.setTint(R.color.green_lime)
                }
                if (valueIndicator in 51..75) {
                    imgCapacityIndicator.setTint(R.color.yellow_tangerine)
                }
                if (valueIndicator > 75) {
                    imgCapacityIndicator.setTint(R.color.red_orange)

                    root.setOnClickListener {
                        onClick?.invoke(data)
                    }
                }
            }
        }
    }

    fun setOnClickData(onClick: (data: Repair) -> Unit) {
        this.onClick = onClick
    }
}