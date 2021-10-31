package com.dicoding.fcmapplication.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.databinding.ActivitySplashBinding

class SplashActivity : BaseActivityBinding<ActivitySplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() =  { ActivitySplashBinding.inflate(it) }

    override fun setupView() {
        Handler(Looper.getMainLooper()).postDelayed({
            // TODO: 26/10/2021 Make condition 
        }, 2000L)
    }

}