package com.dicoding.fcmapplication.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.fcmapplication.domain.model.FdtDetail

class DiffUtilCoveredFatClass(private val oldList: List<FdtDetail.FatList>, private val newList: List<FdtDetail.FatList>): DiffUtil.Callback() {

    //private val oldListData = ArrayList<FdtDetail.FatList>()
    //private val newListData = ArrayList<FdtDetail.FatList>()


    /*fun submitList(oldList: List<FdtDetail.FatList>, newList: List<FdtDetail.FatList>) {
            oldListData.addAll(oldList)
            newListData.addAll(newList)
    }*/

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].fatId == newList[newItemPosition].fatId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].fatId != newList[newItemPosition].fatId -> {
                false
            }
            oldList[oldItemPosition].fatName != newList[newItemPosition].fatName -> {
                false
            }
            else -> true
        }
    }
}