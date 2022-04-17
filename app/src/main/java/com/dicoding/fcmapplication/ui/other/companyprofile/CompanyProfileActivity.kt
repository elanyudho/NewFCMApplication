package com.dicoding.fcmapplication.ui.other.companyprofile

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityCompanyProfileBinding
import com.dicoding.fcmapplication.domain.model.CompanyProfile
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.glide
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompanyProfileActivity : BaseActivityBinding<ActivityCompanyProfileBinding>(),
    Observer<CompanyProfileViewModel.CompanyProfileUiState> {

    @Inject
    lateinit var mViewModel: CompanyProfileViewModel

    override val bindingInflater: (LayoutInflater) -> ActivityCompanyProfileBinding
        get() = { ActivityCompanyProfileBinding.inflate(layoutInflater) }

    override fun setupView() {

        mViewModel.uiState.observe(this, this)
        mViewModel.getCompanyProfile()

        with(binding) {
            headerCompanyProfile.btnBack.setOnClickListener { onBackPressed() }
            headerCompanyProfile.tvTitleHeader.text = getString(R.string.company_profile)
            tvTitleDetailPartner.text

        }
    }

    override fun onChanged(state: CompanyProfileViewModel.CompanyProfileUiState?) {
        when(state){
            is CompanyProfileViewModel.CompanyProfileUiState.CompanyProfileLoaded -> {
                initView(state.data)

            }
            is CompanyProfileViewModel.CompanyProfileUiState.LoadingCompanyProfile -> {
                if (state.isLoading) {
                    with(binding){
                        cvLottieLoading.visible()
                        viewCompanyProfile.gone()
                    }
                }else {
                    with(binding){
                        cvLottieLoading.gone()
                        viewCompanyProfile.visible()
                    }
                }
            }
            is CompanyProfileViewModel.CompanyProfileUiState.FailedLoadCompanyProfile -> {
                this.fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
            }
        }
    }

    private fun initView(data: CompanyProfile) {
        with(binding) {
            tvTitleDetailPartner.text = data.companyName
            tvDetailCompanyProfile.text = data.companyDetail
            data.companyImage?.let { imageCompanyProfile.glide(this@CompanyProfileActivity, it) }
            data.companyBanner?.let { imageBanner.glide(this@CompanyProfileActivity, it) }
        }
    }
}