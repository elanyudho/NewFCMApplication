package com.dicoding.fcmapplication.ui.other.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dicoding.fcmapplication.databinding.FragmentBackConfirmationDialogBinding

class BackConfirmationDialogFragment : DialogFragment() {

    private var _binding: FragmentBackConfirmationDialogBinding? = null

    private val binding
        get() = if (_binding == null) {
            throw IllegalArgumentException("View binding is not initialized yet")
        } else _binding!!

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBackConfirmationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnYes.setOnClickListener {
            requireActivity().finish()
            dismiss()
        }
    }
}