package com.dicoding.fcmapplication.ui.fdt.dialog

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseDialogBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.DialogFilterFdtFragmentBinding
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.ui.dialogfilter.bottomdialogregion.BottomDialogRegionFragment
import com.dicoding.fcmapplication.utils.extensions.disable
import com.dicoding.fcmapplication.utils.extensions.enable
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@AndroidEntryPoint
class FdtFilterDialogFragment(
    private val filter: Filter,
    private val onFilter: (filter: Filter) -> Unit
) : BaseDialogBinding<DialogFilterFdtFragmentBinding>(isFullScreen = false),
    Observer<FdtFilterViewModel.FdtFilterUiState> {

    @Inject
    lateinit var mViewModel: FdtFilterViewModel

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> DialogFilterFdtFragmentBinding
        get() = { layoutInflater, viewGroup, b ->
            DialogFilterFdtFragmentBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    override fun setupView() {

        with(mViewModel){
            uiState.observe(viewLifecycleOwner,this@FdtFilterDialogFragment)
            getRegionList()
        }

        with(binding) {
            btnClose.setOnClickListener { dismiss() }
            btnApplyFilter.setOnClickListener {
                if (filter.region.isNullOrEmpty() || filter.region == ""){
                    requireActivity().fancyToast(
                        getString(R.string.error_filter_is_empty),
                        FancyToast.WARNING
                    )
                }else{
                    onFilter.invoke(filter)
                    dismiss()
                }
            }
        }

        setTextFilterValue(filter)
    }

    override fun onChanged(state: FdtFilterViewModel.FdtFilterUiState?) {
        when(state) {
            is FdtFilterViewModel.FdtFilterUiState.SuccessRegionLoaded -> {
                enable(binding.btnApplyFilter)
                binding.filterRegion.setOnClickFilterListener {
                    setRegionSpinner(state.region)
                }
            }
            is FdtFilterViewModel.FdtFilterUiState.Loading -> {
                disable(binding.btnApplyFilter)
            }
            is FdtFilterViewModel.FdtFilterUiState.Failed -> {
                enable(binding.btnApplyFilter)
                requireContext().fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }
        }
    }

    private fun setRegionSpinner(listData: List<Region>) {
        //init dialog
        val bottomDialogRegion = BottomDialogRegionFragment()
        bottomDialogRegion.show(
            childFragmentManager,
            BottomDialogRegionFragment::class.java.simpleName
        )

        bottomDialogRegion.setOnClickItemListener(
            listData = listData,
            titleDialog = getString(R.string.region)
        ) { data ->
            //set region field
            filter.region = data
            binding.filterRegion.setTextFilter(data)
            bottomDialogRegion.dismiss()
        }
    }

    private fun setTextFilterValue(filter: Filter) {
        if (filter.region.isNullOrEmpty() || filter.region == "") {
            return
        }else {
            binding.filterRegion.setTextFilter(filter.region)
        }
    }

    @Parcelize
    data class Filter(
        var search: String = "",
        var region: String = "",
    ) : Parcelable

    companion object {
        fun build(
            filter: Filter,
            onFilter: (filter: Filter) -> Unit
        ) = FdtFilterDialogFragment(filter, onFilter)
    }
}