package com.dicoding.fcmapplication.ui.other.repairlist

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivityRepairListBinding
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.ui.fdt.main.FdtViewModel
import com.dicoding.fcmapplication.ui.other.adapter.RepairListAdapter
import com.dicoding.fcmapplication.utils.extensions.dp
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

    private var paginator: RecyclerViewPaginator? = null

    private var listTab = listOf<String>("FDT", "FAT")

    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null

    override val bindingInflater: (LayoutInflater) -> ActivityRepairListBinding
        get() = { ActivityRepairListBinding.inflate(layoutInflater) }

    override fun setupView() {
        mViewModel.uiState.observe(this, this)

        binding.headerRepairList.btnBack.setOnClickListener { onBackPressed() }
        binding.headerRepairList.tvTitleHeader.text = getString(R.string.repair_list)

        setRepairListActions()
        setRepairListPagination()
    }

    override fun onChanged(state: RepairListViewModel.RepairListUiState?) {
        when (state) {
            is RepairListViewModel.RepairListUiState.FdtLoaded -> {
                stopLoading()

                val fdtRepairList = ArrayList<Repair>()
                state.list.map {
                    val data = Repair(
                        deviceName = it.fdtName,
                        deviceCoreTotal = it.fdtCore,
                        deviceNote = it.fdtNote,
                        deviceIsService = it.fdtIsService
                    )
                    fdtRepairList.add(data)
                }
                repairListAdapter.appendList(fdtRepairList)
            }
            is RepairListViewModel.RepairListUiState.FatLoaded -> {
                stopLoading()

                val fatRepairList = ArrayList<Repair>()
                state.list.map {
                    val data = Repair(
                        deviceName = it.fatName,
                        deviceCoreTotal = it.fatCore,
                        deviceNote = it.fatNote,
                        deviceIsService = it.fatIsService
                    )
                    fatRepairList.add(data)
                }
                repairListAdapter.appendList(fatRepairList)
            }
            is RepairListViewModel.RepairListUiState.InitialLoading -> {
                startInitialLoading()
                setTabItems()
                setTabAction()
            }
            is RepairListViewModel.RepairListUiState.PagingLoading -> {
                startPagingLoading()
            }
            is RepairListViewModel.RepairListUiState.FailedLoad -> {
                this.fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
            }
        }
    }

    private fun setRepairListPagination() {
        paginator = RecyclerViewPaginator(binding.rvRepairList.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            if (tabChoose == FDT) {
                if (session.user?.isCenterAdmin == true) {
                    mViewModel.getFdtList(NO_ZONE, page)
                } else {
                    session.user?.region?.let { mViewModel.getFdtList(it, page) }
                }

            } else {
                if (session.user?.isCenterAdmin == true) {
                    mViewModel.getFatList(NO_ZONE, 1)
                } else {
                    session.user?.region?.let { mViewModel.getFatList(it, page) }
                }
            }
        }
        paginator?.let { binding.rvRepairList.addOnScrollListener(it) }
    }

    private fun setRepairListActions() {
        with(binding.rvRepairList) {
            adapter = repairListAdapter
            setHasFixedSize(true)

            repairListAdapter.setOnClickData {
                if (it.deviceName?.contains("fdt") == true) {
                    val intent = Intent(this@RepairListActivity, FdtDetailActivity::class.java)
                    intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.deviceName)
                    startActivity(intent)
                }
                if (it.deviceName?.contains("fat") == true) {
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
                        mViewModel.getFdtList(NO_ZONE, 1)
                    } else {
                        session.user?.region?.let { mViewModel.getFdtList(it, 1) }
                    }

                } else {
                    if (session.user?.isCenterAdmin == true) {
                        mViewModel.getFatList(NO_ZONE, 1)
                    } else {
                        session.user?.region?.let { mViewModel.getFatList(it, 1) }
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
            binding.tabLayoutCourse.addOnTabSelectedListener(it)
        }
    }

    private fun setTabItems() {
        with(binding.tabLayoutCourse) {
            val tabList = mutableListOf<String>()
            listTab.forEach {
                tabList.add(it)
            }
            // Add tabs
            addTitleOnlyTabs(tabList)

            // Set margins
            setTabsMargin(0, 6.dp, 0.dp, 6.dp)
        }
    }

    private fun startInitialLoading() {
        binding.rvRepairList.gone()
        binding.progressRepairList.visible()
    }

    private fun stopLoading() {
        binding.rvRepairList.visible()
        binding.progressRepairList.gone()
    }

    private fun startPagingLoading() {
        binding.progressRepairList.visible()
    }

    companion object {
        const val FDT = "FDT"
        const val FAT = "FAT"

        const val NO_ZONE = "%00"
    }

}