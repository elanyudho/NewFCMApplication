package com.dicoding.fcmapplication.ui.fat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.databinding.FragmentFatBinding
import com.dicoding.fcmapplication.utils.extensions.gone

class FatFragment : BaseFragmentBinding<FragmentFatBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFatBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentFatBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun setupView() {
        binding.rvFat.gone()
        binding.searchFat.gone()
    }

}