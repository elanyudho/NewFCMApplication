package com.dicoding.fcmapplication.ui.dialogfilter.bottomdialogchoosefdt

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
import com.dicoding.fcmapplication.databinding.FragmentBottomDialogChooseFdtBinding
import com.dicoding.fcmapplication.domain.model.FdtToAdd
import com.dicoding.fcmapplication.ui.dialogfilter.adapter.FilterBottomDialogChooseFdtAdapter
import com.dicoding.fcmapplication.ui.dialogfilter.adapter.FilterBottomDialogCoveredFatAdapter
import com.dicoding.fcmapplication.ui.other.adddata.addfat.AddFatViewModel
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
class BottomDialogChooseFdtFragment : BaseBottomDialogBinding<FragmentBottomDialogChooseFdtBinding>(), Observer<AddFatViewModel.AddFatUiState> {

    @Inject
    lateinit var mViewModel: AddFatViewModel

    private val fatAdapter : FilterBottomDialogChooseFdtAdapter by lazy { FilterBottomDialogChooseFdtAdapter() }
    private var onClick :((FdtToAdd) -> Unit)? = null

    var title = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBottomDialogChooseFdtBinding
        get() = {layoutInflater, viewGroup, b ->
            FragmentBottomDialogChooseFdtBinding.inflate(layoutInflater,viewGroup,b)
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
            mViewModel.uiState.observe(viewLifecycleOwner, this@BottomDialogChooseFdtFragment)
            mViewModel.getChooseFdtList("")
            binding.tvTitleFilter.text = title
            binding.recyclerBottomSheet.adapter = fatAdapter
            binding.recyclerBottomSheet.addItemDecoration(MarginItemDecoration(spaceSize = 32.dp,leftMargin = 24.dp,rightMargin = 24.dp,bottomMargin = 16.dp))

        }

        callOnceWhenDisplayed {

            with(binding) {
                searchFdt.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        mViewModel.getChooseFdtList(newText.toString())
                        return true
                    }

                })
            }
        }

    }

    override fun onChanged(state: AddFatViewModel.AddFatUiState?) {
        when(state){
            is AddFatViewModel.AddFatUiState.ChooseFdtListLoaded -> {
                setItemsFilter(state.list)
            }
            is AddFatViewModel.AddFatUiState.Loading -> {

            }
            is AddFatViewModel.AddFatUiState.FailedLoadedOrTransaction -> {
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

    fun setOnClickItemListener(titleDialog: String, onClick: (FdtToAdd)  -> Unit){
        setTitleDialog(titleDialog)
        fatAdapter.setOnClickItemListener{ data -> onClick.invoke(data) }
        this.onClick = onClick
    }

    private fun setItemsFilter(listData: List<FdtToAdd>) {
        fatAdapter.submitList(listData)
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