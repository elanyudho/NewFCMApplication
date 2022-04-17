package com.dicoding.fcmapplication.ui.fdt.fdtdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityFdtDetailBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.adapter.CoveredAdapter
import com.dicoding.fcmapplication.ui.fdt.more.MoreFatCoveredActivity
import com.dicoding.fcmapplication.ui.location.LocationActivity
import com.dicoding.fcmapplication.ui.other.adddata.addfdt.AddFdtActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FdtDetailActivity : BaseActivityBinding<ActivityFdtDetailBinding>(),
    Observer<FdtDetailViewModel.FdtDetailUiState> {

    @Inject
    lateinit var mViewModel: FdtDetailViewModel

    @Inject
    lateinit var session: Session

    private val coveredAdapter: CoveredAdapter by lazy { CoveredAdapter() }

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)}

    private lateinit var fdtName: String

    private var clicked = false

    private var fdtDetail: FdtDetail? = null

    private var resultLauncher : ActivityResultLauncher<Intent>? = null

    override val bindingInflater: (LayoutInflater) -> ActivityFdtDetailBinding
        get() = { ActivityFdtDetailBinding.inflate(layoutInflater) }

    override fun setupView() {
        fdtName = intent.getStringExtra(EXTRA_DETAIL_FDT) ?: ""

        with(mViewModel) {
            uiState.observe(this@FdtDetailActivity, this@FdtDetailActivity)
            getFdtDetail(fdtName)
        }

        setResultLauncher()

        binding.headerFdtDetail.btnBack.setOnClickListener { onBackPressed() }
        binding.headerFdtDetail.tvTitleHeader.text = getString(R.string.fdt_profile)

        with(binding){
            fabMenu.setOnClickListener {
                onAddButtonClicked()
            }
            fabEdit.setOnClickListener {
                val intent = Intent(this@FdtDetailActivity, AddFdtActivity::class.java)
                val extras = Bundle()
                extras.putString(AddFdtActivity.PURPOSE_OPEN, AddFdtActivity.TO_EDIT)
                extras.putParcelable(AddFdtActivity.FDT_DETAIL, fdtDetail)
                intent.putExtras(extras)
                resultLauncher?.launch(intent)
            }
            fabDelete.setOnClickListener {
                mViewModel.deleteFdt(fdtDetail?.fdtId.toString())
            }
        }
    }

    override fun onChanged(state: FdtDetailViewModel.FdtDetailUiState?) {
        val rowFatLoss = binding.rowFatLoss.layoutParams as ConstraintLayout.LayoutParams

        when (state) {
            is FdtDetailViewModel.FdtDetailUiState.FdtDetailLoaded -> {

                initFdtDetailView(state.data)

                fdtDetail = state.data

                coveredAdapter.submitList(state.data.fatCoveredList)
                setFdtActions()

                val dataFat = arrayListOf<FdtDetail.FatList>()

                if (state.data.fatCoveredList.isEmpty()){
                    with(binding){
                        rowFatLoss.topToBottom = tvNoFatCovered.id
                        rowFatLoss.topMargin = 24.dp
                        rvFatCovered.gone()
                        grpEmptyFatCoveredView.visible()
                    }
                }else{
                    with(binding) {
                        rowFatLoss.topToBottom = rvFatCovered.id
                        rowFatLoss.topMargin = 8.dp
                        rvFatCovered.visible()
                        grpEmptyFatCoveredView.gone()

                        dataFat.addAll(state.data.fatCoveredList)
                        rowFatCovered.setOnClickListener {
                            val intent = Intent(this@FdtDetailActivity, MoreFatCoveredActivity::class.java)
                            intent.putParcelableArrayListExtra(MoreFatCoveredActivity.EXTRA_FAT_COVERED, dataFat)
                            startActivity(intent)
                        }
                    }
                }
            }
            is FdtDetailViewModel.FdtDetailUiState.SuccessDeleteFdt -> {
                fancyToast(getString(R.string.success_delete_fdt), FancyToast.SUCCESS)
                setResult(Activity.RESULT_OK)
                onBackPressed()
            }
            is FdtDetailViewModel.FdtDetailUiState.Loading -> {
                if (state.isLoading){
                    with(binding){
                        viewFdtDetail.gone()
                        cvLottieLoading.visible()
                        fabMenu.invisible()
                        disable(binding.fabMenu)
                    }
                }else{
                    with(binding){
                        viewFdtDetail.visible()
                        cvLottieLoading.gone()
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
            is FdtDetailViewModel.FdtDetailUiState.Failed -> {
                state.failure.throwable.printStackTrace()
                fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }
        }
    }

    private fun setFdtActions() {
        with(binding.rvFatCovered) {
            adapter = coveredAdapter
            setHasFixedSize(true)

            coveredAdapter.setOnClickData {
                val intent = Intent(this@FdtDetailActivity, FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initFdtDetailView(obj: FdtDetail){
        with(binding){
            obj.fdtCoreUsed?.let { obj.fdtCore?.let { core -> setArcProgressBar(core, it) } }
            tvArcBarLocationName.text = "Core are used in ${obj.fdtName}"
            tvCoreTotal.text = obj.fdtCore
            tvBackup.text = obj.fdtCoreRemaining
            tvCoreUsed.text = obj.fdtCoreUsed
            tvFatLossNumber.text = obj.fdtLoss + " db"
            tvFatNumber.text = obj.fdtCoveredFat + " FAT Covered"
            tvRepairNotes.text = if(obj.fdtNote == null || obj.fdtNote == "") "No note" else obj.fdtNote
            if (obj.fdtIsService == true){
                icRepair.visible()
            }else {
                icRepair.invisible()
            }
            cvLocation.setOnClickListener {
                val intent = Intent(this@FdtDetailActivity, LocationActivity::class.java)
                val extras = Bundle()
                extras.putInt(LocationActivity.EXTRA_TYPE, 1)
                extras.putString(LocationActivity.EXTRA_COORDINATE, obj.fdtLocation)
                extras.putString(LocationActivity.EXTRA_NAME, obj.fdtName)
                intent.putExtras(extras)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setArcProgressBar(allFdtCoreTotal: String, allFdtCoreUsed: String){
        val percentValue =  allFdtCoreUsed.toDouble()/allFdtCoreTotal.toDouble()*100
        val percentValueInt = percentValue.toInt()
        val percentValueStr = percentValueInt.toString()
        with(binding){
            semiCircleArcProgressBar.setPercent(percentValueInt)
            tvCapacityPercentage.text = "$percentValueStr%"
        }

    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mViewModel.getFdtDetail(fdtName)
                setResult(Activity.RESULT_OK)
            }
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
        const val EXTRA_DETAIL_FDT = "detail fdt"
    }
}