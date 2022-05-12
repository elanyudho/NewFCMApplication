package com.dicoding.fcmapplication.ui.other.repairlist

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityRepairListBinding
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.ui.other.adapter.RepairListAdapter
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.google.android.material.tabs.TabLayout
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class RepairListActivity : BaseActivityBinding<ActivityRepairListBinding>(),
    Observer<RepairListViewModel.RepairListUiState> {

    @Inject
    lateinit var mViewModel: RepairListViewModel

    @Inject
    lateinit var session: Session

    var tabChoose = "FDT"

    private val repairListAdapter: RepairListAdapter by lazy { RepairListAdapter() }

    private var listTab = listOf("FDT", "FAT")

    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null

    override val bindingInflater: (LayoutInflater) -> ActivityRepairListBinding
        get() = { ActivityRepairListBinding.inflate(layoutInflater) }

    override fun setupView() {
        mViewModel.uiState.observe(this, this)

        binding.headerRepairList.btnBack.setOnClickListener { onBackPressed() }
        binding.headerRepairList.tvTitleHeader.text = getString(R.string.repair_list)

        setRepairListActions()
        setTabAction()
        setTabItems()

    }

    override fun onChanged(state: RepairListViewModel.RepairListUiState?) {
        when (state) {
            is RepairListViewModel.RepairListUiState.FdtLoaded -> {
                stopLoading()

                val fdtRepairList = ArrayList<Repair>()
                state.list.map {
                    if(it.fdtIsService == true){
                        val data = Repair(
                            deviceName = it.fdtName,
                            deviceCoreTotal = it.fdtCore,
                            deviceNote = it.fdtNote,
                            deviceIsService = it.fdtIsService,
                            deviceCoreUsed = it.fdtCoreUsed
                        )
                        fdtRepairList.add(data)
                    }
                }
                repairListAdapter.submitList(fdtRepairList)
            }
            is RepairListViewModel.RepairListUiState.FatLoaded -> {
                stopLoading()

                val fatRepairList = ArrayList<Repair>()
                state.list.map {
                    if(it.fatIsService == true){
                        val data = Repair(
                            deviceName = it.fatName,
                            deviceCoreTotal = it.fatCore,
                            deviceNote = it.fatNote,
                            deviceIsService = it.fatIsService,
                            deviceCoreUsed = it.fatCoreUsed
                        )
                        fatRepairList.add(data)
                    }
                }
                repairListAdapter.submitList(fatRepairList)
            }
            is RepairListViewModel.RepairListUiState.Loading -> {
                startLoading()
            }
            is RepairListViewModel.RepairListUiState.FailedLoad -> {
                this.fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
            }
        }
    }

    private fun setRepairListActions() {
        with(binding.rvRepairList) {
            adapter = repairListAdapter
            setHasFixedSize(true)

            repairListAdapter.setOnClickData {
                if (it.deviceName?.contains("FDT", true) == true) {
                    val intent = Intent(this@RepairListActivity, FdtDetailActivity::class.java)
                    intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.deviceName)
                    startActivity(intent)
                }
                if (it.deviceName?.contains("FAT", true) == true) {
                    val intent = Intent(this@RepairListActivity, FatDetailActivity::class.java)
                    intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.deviceName)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setTabAction() {
        onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabChoose = tab?.position?.let { listTab[it] }.toString()
                d("Selected Tab: $tabChoose.toString()")
                if (tabChoose == FDT) {
                    if (session.user?.isCenterAdmin == true) {
                        mViewModel.getFdtList(NO_ZONE)
                    } else {
                        session.user?.region?.let { mViewModel.getFdtList(it) }
                    }

                } else {
                    if (session.user?.isCenterAdmin == true) {
                        mViewModel.getFatList(NO_ZONE)
                    } else {
                        session.user?.region?.let { mViewModel.getFatList(it) }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                d("UNSELECTED: ${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tabChoose = tab?.position?.let { listTab[it] }.toString()
                d("RESELECTED TAB TITLE: ${tab?.text} ,ID CATEGORY : $tabChoose")

            }
        }
        onTabSelectedListener?.let {
            binding.tabLayoutRepair.addOnTabSelectedListener(it)
        }
    }

    private fun setTabItems() {
        with(binding.tabLayoutRepair) {
            val tabList = mutableListOf<String>()
            listTab.forEach {
                tabList.add(it)
            }
            // Add tabs
            addTitleOnlyTabs(tabList)

        }
    }

    private fun startLoading() {
        binding.rvRepairList.gone()
        binding.progressRepairList.visible()
    }

    private fun stopLoading() {
        binding.rvRepairList.visible()
        binding.progressRepairList.gone()
    }

    companion object {
        const val FDT = "FDT"
        const val FAT = "FAT"

        const val NO_ZONE = ""
    }

}