package com.dicoding.fcmapplication.ui.other.companyprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityCompanyProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyProfileActivity : BaseActivityBinding<ActivityCompanyProfileBinding>()
{
    override val bindingInflater: (LayoutInflater) -> ActivityCompanyProfileBinding
        get() = { ActivityCompanyProfileBinding.inflate(layoutInflater) }

    override fun setupView() {
        with(binding){
            headerCompanyProfile.btnBack.setOnClickListener { onBackPressed() }
            headerCompanyProfile.tvTitleHeader.text = getString(R.string.company_profile)
            tvTitleDetailPartner.text

        }
    }
}