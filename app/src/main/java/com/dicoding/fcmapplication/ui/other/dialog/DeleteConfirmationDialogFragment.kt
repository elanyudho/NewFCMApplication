package com.dicoding.fcmapplication.ui.other.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseDialogBinding
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentDeleteConfirmationDialogBinding

class DeleteConfirmationDialogFragment : BaseDialogBinding<FragmentDeleteConfirmationDialogBinding>(false) {

    private var tvFdtName = ""

    private var onClick :((Boolean) -> Unit)? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDeleteConfirmationDialogBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentDeleteConfirmationDialogBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    override fun setupView() {
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnYes.setOnClickListener {
            onClick?.invoke(true)
            dismiss()
        }
    }

    fun setConfirmationListener(device: String, onClick: (Boolean)  -> Unit){
        if (device == FDT) {
            binding.materialTextView3.setText(R.string.fdt_delete_confirmation)
        } else {
            binding.materialTextView3.setText(R.string.fat_delete_confirmation)
        }
        this.onClick = onClick
    }

    companion object {
        const val FDT = "detail fdt"
        const val FAT = "detail fat"
    }

}