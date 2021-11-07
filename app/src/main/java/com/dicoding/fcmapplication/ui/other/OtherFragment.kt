package com.dicoding.fcmapplication.ui.other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentOtherBinding

class OtherFragment : BaseFragmentBinding<FragmentOtherBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOtherBinding
        get() = {layoutInflater, viewGroup, b ->
            FragmentOtherBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun setupView() {
        TODO("Not yet implemented")
    }

}