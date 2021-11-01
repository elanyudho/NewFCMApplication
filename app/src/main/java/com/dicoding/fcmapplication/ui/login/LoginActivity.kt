package com.dicoding.fcmapplication.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityLoginBinding

class LoginActivity : BaseActivityBinding<ActivityLoginBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = { ActivityLoginBinding.inflate(it) }

    override fun setupView() {
        TODO("Not yet implemented")
    }


}