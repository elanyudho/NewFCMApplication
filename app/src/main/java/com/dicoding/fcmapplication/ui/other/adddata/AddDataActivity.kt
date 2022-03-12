package com.dicoding.fcmapplication.ui.other.adddata

import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityAddDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDataActivity : BaseActivityBinding<ActivityAddDataBinding>() {


    override val bindingInflater: (LayoutInflater) -> ActivityAddDataBinding
        get() = { ActivityAddDataBinding.inflate(layoutInflater) }

    override fun setupView() {

        with(binding){
            headerAddData.tvTitleHeader.setText(R.string.add_fd_fat)
            headerAddData.btnBack.setOnClickListener {
                onBackPressed()
            }
            rowAddFdt.setOnClickListener {

            }
            rowAddFat.setOnClickListener {

            }
        }
    }
}