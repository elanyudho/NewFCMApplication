package com.dicoding.fcmapplication.ui.other.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseRecyclerViewAdapter
import com.dicoding.core.abstraction.BaseViewHolder
import com.dicoding.core.abstraction.PagingRecyclerViewAdapter
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ItemDeviceLinearLayoutBinding
import com.dicoding.fcmapplication.databinding.ItemServiceListBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.utils.extensions.invisible
import com.dicoding.fcmapplication.utils.extensions.setTint
import com.dicoding.fcmapplication.utils.extensions.visible

class RepairListAdapter : BaseRecyclerViewAdapter<RepairListAdapter.RepairListViewHolder>() {


    private var onClick: ((Repair) -> Unit)? = null

    private var valueIndicator = 0

    private var listData = mutableListOf<Repair>()

    fun submitList(newList: List<Repair>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }


    override val holderInflater: (LayoutInflater, ViewGroup, Boolean) -> RepairListViewHolder
        get() = { inflater, viewGroup, boolean ->
            RepairListViewHolder(
                ItemServiceListBinding.inflate(
                    inflater,
                    viewGroup,
                    boolean
                )
            )
        }

    override fun onBindViewHolder(holder: RepairListViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
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

                imgCapacityIndicator.setTint(
                    when {
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

    fun setOnClickData(onClick: (data: Repair) -> Unit) {
        this.onClick = onClick
    }
}
