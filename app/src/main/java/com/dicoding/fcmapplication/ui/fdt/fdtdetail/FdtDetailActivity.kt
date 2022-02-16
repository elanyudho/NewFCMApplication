package com.dicoding.fcmapplication.ui.fdt.fdtdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityFdtDetailBinding
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.adapter.FatCoveredAdapter
import com.dicoding.fcmapplication.ui.fdt.adapter.FatHorizontalAdapter
import com.dicoding.fcmapplication.ui.fdt.more.MoreFatCoveredActivity
import com.dicoding.fcmapplication.ui.location.LocationActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FdtDetailActivity : BaseActivityBinding<ActivityFdtDetailBinding>(),
    Observer<FdtDetailViewModel.FdtDetailUiState> {

    @Inject
    lateinit var mViewModel: FdtDetailViewModel

    private val fatHorizontalAdapter: FatCoveredAdapter by lazy { FatCoveredAdapter() }

    private lateinit var uuid: String

    override val bindingInflater: (LayoutInflater) -> ActivityFdtDetailBinding
        get() = { ActivityFdtDetailBinding.inflate(layoutInflater) }

    override fun setupView() {
        uuid = intent.getStringExtra(EXTRA_DETAIL_FDT) ?: ""

        with(mViewModel) {
            uiState.observe(this@FdtDetailActivity, this@FdtDetailActivity)
            getFdtDetail(uuid)
        }

        binding.headerFdtDetail.btnBack.setOnClickListener { onBackPressed() }
        binding.headerFdtDetail.tvTitleHeader.text = getString(R.string.fdt_profile)
    }

    override fun onChanged(state: FdtDetailViewModel.FdtDetailUiState?) {
        when (state) {
            is FdtDetailViewModel.FdtDetailUiState.FdtDetailLoaded -> {
                initFdtDetailView(state.data)

                fatHorizontalAdapter.submitList(state.data.fatCoveredList)
                setFdtActions()

                state.data.fdtCore?.let { state.data.fdtCoreUsed?.let { it1 ->
                    state.data.fdtCoreRemaining?.let { it2 ->
                        setArcProgressBar(it,
                            it1, it2
                        )
                    }
                } }

                fatHorizontalAdapter.valueIndicator = state.data.fdtCore?.toInt()!!

                val dataFat = arrayListOf<FdtDetail.FatList>()

                if (state.data.fatCoveredList.isEmpty()){
                    with(binding){
                        rvFatCovered.invisible()
                        imageNoFatCovered.visible()
                        tvNoFatCovered.visible()
                    }
                }else{
                    binding.rvFatCovered.visible()
                    binding.imageNoFatCovered.gone()
                    binding.tvNoFatCovered.gone()
                    dataFat.addAll(state.data.fatCoveredList)
                    binding.rowFatCovered.setOnClickListener {
                        val intent = Intent(this@FdtDetailActivity, MoreFatCoveredActivity::class.java)
                        intent.putParcelableArrayListExtra(MoreFatCoveredActivity.EXTRA_FAT_COVERED, dataFat)
                        startActivity(intent)
                    }
                }

            }
            is FdtDetailViewModel.FdtDetailUiState.LoadingFdtDetail -> {

            }
            is FdtDetailViewModel.FdtDetailUiState.FailedLoadFdtDetail -> {
                state.failure.throwable.printStackTrace()
                fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            }
        }
    }

    private fun setFdtActions() {
        with(binding.rvFatCovered) {
            adapter = fatHorizontalAdapter
            setHasFixedSize(true)

            fatHorizontalAdapter.setOnClickData {
                val intent = Intent(this@FdtDetailActivity, FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initFdtDetailView(obj: FdtDetail){
        with(binding){
            tvArcBarLocationName.text = "Core are used in ${obj.fdtName}"
            tvCoreTotal.text = obj.fdtCore
            tvBackup.text = obj.fdtCoreRemaining
            tvCoreUsed.text = obj.fdtCoreUsed
            tvFatLossNumber.text = obj.fdtLoss
            tvFatNumber.text = obj.fdtCoveredFat
            tvRepairNotes.text = if(obj.fdtNote == ""){
                "No note"
            }else{
                obj.fdtNote
            }
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
    private fun setArcProgressBar(allFdtCoreTotal: String, allFdtCoreUsed: String, allFdtCoreBackup: String){
        val percentValue =  allFdtCoreUsed.toDouble()/allFdtCoreTotal.toDouble()*100
        val percentValueInt = percentValue.toInt()
        val percentValueStr = percentValueInt.toString()
        with(binding){
            semiCircleArcProgressBar.setPercent(percentValueInt)
            tvCoreTotal.text = allFdtCoreTotal
            tvCoreUsed.text = allFdtCoreUsed
            tvBackup.text = allFdtCoreBackup
            tvCapacityPercentage.text = "$percentValueStr%"
        }

    }

    companion object {
        const val EXTRA_DETAIL_FDT = "detail fdt"
    }
}