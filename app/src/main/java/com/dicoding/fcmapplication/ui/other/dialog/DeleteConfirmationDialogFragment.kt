package com.dicoding.fcmapplication.ui.other.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseDialogBinding
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentDeleteConfirmationDialogBinding

class DeleteConfirmationDialogFragment : BaseDialogBinding<FragmentDeleteConfirmationDialogBinding>(false) {

    private var onClick :((Boolean) -> Unit)? = null

    private var deviceType = FDT


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDeleteConfirmationDialogBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentDeleteConfirmationDialogBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    override fun setupView() {

        setTextDialog(deviceType)

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnYes.setOnClickListener {
            onClick?.invoke(true)
            dismiss()
        }
    }

    fun setConfirmationListener(device: String, onClick: (Boolean)  -> Unit){
        deviceType = device
        this.onClick = onClick
    }

    private fun setTextDialog(deviceType: String) {
        if (deviceType == FDT) {
            binding.materialTextView3.text = getString(R.string.fdt_delete_confirmation)
        } else {
            binding.materialTextView3.text = getString(R.string.fat_delete_confirmation)
        }
    }

    companion object {
        const val FDT = "detail fdt"
        const val FAT = "detail fat"
    }

}