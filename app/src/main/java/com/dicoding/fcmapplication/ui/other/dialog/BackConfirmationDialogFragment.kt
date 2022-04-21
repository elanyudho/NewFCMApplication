package com.dicoding.fcmapplication.ui.other.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseDialogBinding
import com.dicoding.fcmapplication.databinding.FragmentBackConfirmationDialogBinding

class BackConfirmationDialogFragment : BaseDialogBinding<FragmentBackConfirmationDialogBinding>(false) {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBackConfirmationDialogBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentBackConfirmationDialogBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    override fun setupView() {
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnYes.setOnClickListener {
            requireActivity().finish()
            dismiss()
        }
    }

}