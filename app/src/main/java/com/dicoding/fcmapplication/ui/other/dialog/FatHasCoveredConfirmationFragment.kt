package com.dicoding.fcmapplication.ui.other.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseDialogBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentBackConfirmationDialogBinding
import com.dicoding.fcmapplication.databinding.FragmentFatHasCoveredConfirmationBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail

class FatHasCoveredConfirmationFragment : BaseDialogBinding<FragmentFatHasCoveredConfirmationBinding>(false) {

    private var onClick :((Boolean) -> Unit)? = null

    private var tvFdtName = ""

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFatHasCoveredConfirmationBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentFatHasCoveredConfirmationBinding.inflate(
                layoutInflater,
                viewGroup,
                b
            )
        }

    @SuppressLint("SetTextI18n")
    override fun setupView() {
        binding.tvConfirmation.text = "This FAT has been covered with other FDT. \nDo you want to change this FAT covered with $tvFdtName?"

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnYes.setOnClickListener {
            onClick?.invoke(true)
            dismiss()
        }
    }

    fun setConfirmationListener(fdtName: String, onClick: (Boolean)  -> Unit){
        tvFdtName = fdtName
        this.onClick = onClick
    }

}