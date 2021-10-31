package com.dicoding.fcmapplication.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityOnBoardingBinding
import javax.inject.Inject

class OnBoardingActivity : BaseActivityBinding<ActivityOnBoardingBinding>() {

    @Inject
    lateinit var session: Session

    override val bindingInflater: (LayoutInflater) -> ActivityOnBoardingBinding
        get() = { ActivityOnBoardingBinding.inflate(it) }

    override fun setupView() {
        TODO("Not yet implemented")
    }

}