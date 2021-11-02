package com.dicoding.fcmapplication.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivitySplashBinding
import com.dicoding.fcmapplication.ui.login.LoginActivity
import com.dicoding.fcmapplication.ui.main.MainActivity
import com.dicoding.fcmapplication.ui.onboarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivityBinding<ActivitySplashBinding>() {

    @Inject
    lateinit var session: Session

    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() =  { ActivitySplashBinding.inflate(it) }

    override fun setupView() {
        Handler(Looper.getMainLooper()).postDelayed({
            moveNext()
        }, 2000L)
    }

    private fun moveNext() {
        if (session.isFirstTime) {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
        } else {
            if(session.isLogin){
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent =Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
        finish()
    }

}