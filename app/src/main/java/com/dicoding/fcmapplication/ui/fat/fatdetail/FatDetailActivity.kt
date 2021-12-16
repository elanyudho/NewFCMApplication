package com.dicoding.fcmapplication.ui.fat.fatdetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ScrollView
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityFatDetailBinding
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.ui.location.LocationActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FatDetailActivity : BaseActivityBinding<ActivityFatDetailBinding>(),
    Observer<FatDetailViewModel.FatDetailUiState> {

    @Inject
    lateinit var mViewModel: FatDetailViewModel

    private lateinit var uuid: String

    override val bindingInflater: (LayoutInflater) -> ActivityFatDetailBinding
        get() = { ActivityFatDetailBinding.inflate(layoutInflater) }

    override fun setupView() {
        uuid = intent.getStringExtra(EXTRA_DETAIL_FAT) ?: ""

        with(mViewModel) {
            uiState.observe(this@FatDetailActivity, this@FatDetailActivity)
            getFatDetail(uuid)
        }

        binding.headerFatDetail.tvTitleHeader.text = getString(R.string.fat_profile)
        binding.headerFatDetail.btnBack.setOnClickListener { onBackPressed() }


    }

    override fun onChanged(state: FatDetailViewModel.FatDetailUiState?) {
        when(state) {
            is FatDetailViewModel.FatDetailUiState.FatDetailLoaded -> {
                initFdtDetailView(state.data)
            }
            is FatDetailViewModel.FatDetailUiState.LoadingFatDetail -> {

            }
            is FatDetailViewModel.FatDetailUiState.FailedLoadFatDetail -> {
                state.failure.throwable.printStackTrace()
                fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }
        }
    }

    private fun initFdtDetailView(obj: FatDetail) {
        with(binding) {
            obj.fatImage?.let { imageDetail.glide(this@FatDetailActivity, it) }
            tvNameDetail.text = obj.fatName
            tvFatCore.text = obj.fatCore
            tvFatCoreRemaining.text = obj.fatCoreRemaining
            tvFatCoreUsed.text = obj.fatCoreUsed
            tvFatLossNumber.text = obj.fatLoss
            tvHomeNumber.text = obj.fatCoveredHome
            tvRepairNotes.text = if(obj.fatNote == ""){
                "No note"
            }else{
                obj.fatNote
            }
            if (obj.fatIsService == true) {
                icIsService.visible()
            } else {
                icIsService.invisible()
            }
           cvLocation.setOnClickListener {
                val intent = Intent(this@FatDetailActivity, LocationActivity::class.java)
                val extras = Bundle()
                extras.putInt(LocationActivity.EXTRA_TYPE, 2)
                extras.putString(LocationActivity.EXTRA_COORDINATE, obj.fatLocation)
                extras.putString(LocationActivity.EXTRA_NAME, obj.fatName)
                intent.putExtras(extras)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val EXTRA_DETAIL_FAT = "detail fat"
    }
}