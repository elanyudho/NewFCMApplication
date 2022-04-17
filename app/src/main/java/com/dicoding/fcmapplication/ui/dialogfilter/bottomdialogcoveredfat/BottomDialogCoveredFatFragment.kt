package com.dicoding.fcmapplication.ui.dialogfilter.bottomdialogcoveredfat

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseBottomDialogBinding
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.RequestResults
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentBottomDialogCoveredFatBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.model.FdtToAdd
import com.dicoding.fcmapplication.ui.dialogfilter.adapter.FilterBottomDialogCoveredFatAdapter
import com.dicoding.fcmapplication.ui.other.adddata.addfat.AddFatViewModel
import com.dicoding.fcmapplication.ui.other.adddata.addfdt.AddFdtViewModel
import com.dicoding.fcmapplication.utils.customview.recyclerview.MarginItemDecoration
import com.dicoding.fcmapplication.utils.extensions.dp
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@AndroidEntryPoint
class BottomDialogCoveredFatFragment : BaseBottomDialogBinding<FragmentBottomDialogCoveredFatBinding>(),
    Observer<AddFdtViewModel.AddFdtUiState> {

    @Inject
    lateinit var mViewModel: AddFdtViewModel

    private val adapter : FilterBottomDialogCoveredFatAdapter by lazy { FilterBottomDialogCoveredFatAdapter() }
    private var onClick :((FdtDetail.FatList) -> Unit)? = null

    var title = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBottomDialogCoveredFatBinding
        get() = {layoutInflater, viewGroup, b ->
            FragmentBottomDialogCoveredFatBinding.inflate(layoutInflater,viewGroup,b)
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun setupView() {

        callOnceWhenCreated {
            mViewModel.uiState.observe(viewLifecycleOwner, this)
            mViewModel.getCoveredFatList("")
            binding.tvTitleFilter.text = title
            binding.recyclerBottomSheet.adapter = adapter
            binding.recyclerBottomSheet.addItemDecoration(MarginItemDecoration(spaceSize = 32.dp,leftMargin = 24.dp,rightMargin = 24.dp,bottomMargin = 16.dp))

        }

        callOnceWhenDisplayed {

            with(binding) {
                searchFat.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        mViewModel.getCoveredFatList(newText.toString())
                        return true
                    }

                })
            }
        }

    }

    override fun onChanged(state: AddFdtViewModel.AddFdtUiState?) {
        when(state){
            is AddFdtViewModel.AddFdtUiState.CoveredFatListLoaded -> {
                setItemsFilter(state.list)
            }
            is AddFdtViewModel.AddFdtUiState.Loading -> {

            }
            is AddFdtViewModel.AddFdtUiState.FailedLoadedOrTransaction -> {
                handleFailure(state.failure)
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.Theme_FCMApplication_BottomSheetTheme
    }

    private fun handleFailure(failure: Failure) {
        if (failure.requestResults == RequestResults.NO_CONNECTION) {
            activity?.fancyToast(getString(R.string.error_unstable_network), FancyToast.ERROR)
        } else {
            activity?.fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
        }
    }

    fun setOnClickItemListener(titleDialog: String, onClick: (FdtDetail.FatList)  -> Unit){
        setTitleDialog(titleDialog)
        adapter.setOnClickItemListener{ data -> onClick.invoke(data) }
        this.onClick = onClick
    }

    private fun setItemsFilter(listData: List<FdtDetail.FatList>) {
        adapter.submitList(listData)
    }

    private fun setTitleDialog(title:String){
        this.title = title
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


}