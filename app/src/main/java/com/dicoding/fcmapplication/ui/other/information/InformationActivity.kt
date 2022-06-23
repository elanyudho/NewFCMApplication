package com.dicoding.fcmapplication.ui.other.information

import android.content.Intent
import android.view.LayoutInflater
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityInformationBinding
import com.dicoding.fcmapplication.ui.other.companyprofile.CompanyProfileActivity

class InformationActivity : BaseActivityBinding<ActivityInformationBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityInformationBinding
        get() = { ActivityInformationBinding.inflate(layoutInflater) }

    override fun setupView() {
        setHeaderAction()
        setCompanyProfileAction()
    }

    private fun setHeaderAction() {
        binding.headerInformation.tvTitleHeader.text = getString(R.string.information)
        binding.headerInformation.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setCompanyProfileAction() {
        binding.rowCompanyProfile.setOnClickListener {
            startActivity(Intent(this, CompanyProfileActivity::class.java))
        }
    }


}