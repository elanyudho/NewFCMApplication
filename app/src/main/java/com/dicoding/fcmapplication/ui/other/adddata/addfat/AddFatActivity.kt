package com.dicoding.fcmapplication.ui.other.adddata.addfat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.databinding.ActivityAddFatBinding
import com.dicoding.fcmapplication.databinding.ActivityAddFdtBinding

class AddFatActivity : BaseActivityBinding<ActivityAddFatBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityAddFatBinding
        get() = { ActivityAddFatBinding.inflate(layoutInflater) }

    override fun setupView() {
        TODO("Not yet implemented")
    }

}