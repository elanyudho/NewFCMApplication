package com.dicoding.fcmapplication.ui.fat.fatdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
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

    @Inject
    lateinit var session: Session

    private lateinit var fatName: String

    private var clicked = false

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)}

    override val bindingInflater: (LayoutInflater) -> ActivityFatDetailBinding
        get() = { ActivityFatDetailBinding.inflate(layoutInflater) }

    override fun setupView() {
        fatName = intent.getStringExtra(EXTRA_DETAIL_FAT) ?: ""

        with(mViewModel) {
            uiState.observe(this@FatDetailActivity, this@FatDetailActivity)
            getFatDetail(fatName)
        }

        binding.headerFatDetail.tvTitleHeader.text = getString(R.string.fat_profile)
        binding.headerFatDetail.btnBack.setOnClickListener { onBackPressed() }

        with(binding){
            fabMenu.setOnClickListener {
                onAddButtonClicked()
            }
            fabEdit.setOnClickListener {

            }
            fabDelete.setOnClickListener {

            }
        }

    }

    override fun onChanged(state: FatDetailViewModel.FatDetailUiState?) {
        when(state) {
            is FatDetailViewModel.FatDetailUiState.FatDetailLoaded -> {
                initFdtDetailView(state.data)

                with(binding){
                    cvLottieLoading.gone()
                    viewFatDetail.visible()
                }

            }
            is FatDetailViewModel.FatDetailUiState.LoadingFatDetail -> {
                if(state.isLoading) {
                    with(binding){
                        cvLottieLoading.visible()
                        viewFatDetail.gone()
                    }
                }else {
                    with(binding){
                        cvLottieLoading.gone()
                        viewFatDetail.visible()
                        if (session.user?.isAdmin == true){
                            binding.fabMenu.visible()
                            enable(binding.fabMenu)
                        }else{
                            binding.fabMenu.invisible()
                            disable(binding.fabMenu)
                        }
                    }
                }
            }
            is FatDetailViewModel.FatDetailUiState.FailedLoadFatDetail -> {
                state.failure.throwable.printStackTrace()
                fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initFdtDetailView(obj: FatDetail) {
        with(binding) {
            obj.fatCoreUsed?.let { obj.fatCore?.let { core -> setArcProgressBar(core, it) } }
            tvArcBarLocationName.text = "Core are used in ${obj.fatName}"
            tvCoreTotal.text = obj.fatCore
            tvBackup.text = obj.fatCoreRemaining
            tvCoreUsed.text = obj.fatCoreUsed
            tvFatLossNumber.text = obj.fatLoss
            tvHomeNumber.text = obj.fatCoveredHome
            tvRepairNotes.text = if(obj.fatNote == "null"){
                "No note"
            }else{
                obj.fatNote
            }
            if (obj.fatIsService == true) {
                icRepair.visible()
            } else {
                icRepair.invisible()
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

    @SuppressLint("SetTextI18n")
    private fun setArcProgressBar(allFatCoreTotal: String, allFatCoreUsed: String){
        val percentValue =  allFatCoreUsed.toDouble()/allFatCoreTotal.toDouble()*100
        val percentValueInt = percentValue.toInt()
        val percentValueStr = percentValueInt.toString()
        with(binding){
            semiCircleArcProgressBar.setPercent(percentValueInt)
            tvCapacityPercentage.text = "$percentValueStr%"
        }

    }

    private fun onAddButtonClicked() {
        setVisbility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked){
            with(binding){
                fabEdit.isClickable = true
                fabDelete.isClickable = true
            }
        }else{
            with(binding){
                fabEdit.isClickable = false
                fabDelete.isClickable = false
            }
        }
    }

    private fun setVisbility(clicked: Boolean) {
        if(!clicked){
            with(binding){
                fabEdit.visible()
                fabDelete.visible()
            }
        }else{
            with(binding){
                fabEdit.invisible()
                fabDelete.invisible()
            }
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            with(binding){
                fabEdit.startAnimation(fromBottom)
                fabDelete.startAnimation(fromBottom)
                fabMenu.startAnimation(rotateOpen)
            }
        }else{
            with(binding){
                fabEdit.startAnimation(toBottom)
                fabDelete.startAnimation(toBottom)
                fabMenu.startAnimation(rotateClose)
            }
        }
    }

    companion object {
        const val EXTRA_DETAIL_FAT = "detail fat"
    }
}