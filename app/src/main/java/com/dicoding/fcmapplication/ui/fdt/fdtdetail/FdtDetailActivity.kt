package com.dicoding.fcmapplication.ui.fdt.fdtdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.databinding.ActivityFatDetailBinding

class FdtDetailActivity : BaseActivityBinding<ActivityFatDetailBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityFatDetailBinding
        get() = { ActivityFatDetailBinding.inflate(layoutInflater) }

    override fun setupView() {
        TODO("Not yet implemented")
    }
}