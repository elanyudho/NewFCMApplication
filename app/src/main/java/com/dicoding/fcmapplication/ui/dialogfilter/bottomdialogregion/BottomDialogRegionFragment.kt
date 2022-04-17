package com.dicoding.fcmapplication.ui.dialogfilter.bottomdialogregion

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseBottomDialogBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.BottomFilterListBinding
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.ui.dialogfilter.adapter.FilterBottomDialogAdapter
import com.dicoding.fcmapplication.utils.customview.recyclerview.MarginItemDecoration
import com.dicoding.fcmapplication.utils.extensions.dp


class BottomDialogRegionFragment : BaseBottomDialogBinding<BottomFilterListBinding>() {

    private val adapter : FilterBottomDialogAdapter by lazy { FilterBottomDialogAdapter() }
    private var onClick :((String) -> Unit)? = null

    var title = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> BottomFilterListBinding
        get() = {layoutInflater, viewGroup, b ->
            BottomFilterListBinding.inflate(layoutInflater,viewGroup,b)
        }

    override fun setupView() {
        binding.tvTitleFilter.text = title
        binding.recyclerBottomSheet.adapter = adapter
        binding.recyclerBottomSheet.addItemDecoration(MarginItemDecoration(spaceSize = 32.dp,leftMargin = 24.dp,rightMargin = 24.dp,bottomMargin = 16.dp))
    }

    override fun getTheme(): Int {
        return R.style.Theme_FCMApplication_BottomSheetTheme
    }

    fun setOnClickItemListener(listData:List<Region>, titleDialog: String, onClick: (String)  -> Unit){
        setItemsFilter(listData)
        setTitleDialog(titleDialog)
        adapter.setOnClickItemListener{ data -> onClick.invoke(data) }
        this.onClick = onClick
    }

    private fun setItemsFilter(listData: List<Region>) {
        val listFilter :MutableList<String> = mutableListOf()
        listData.forEach { it.regionName?.let { it1 -> listFilter.add(it1) } }
        adapter.submitList(listFilter)
    }


    private fun setTitleDialog(title:String){
        this.title = title
    }

}