package com.dicoding.fcmapplication.ui.other.repairlist

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityRepairListBinding
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.ui.fdt.main.FdtViewModel
import com.dicoding.fcmapplication.ui.other.adapter.RepairListAdapter
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepairListActivity : BaseActivityBinding<ActivityRepairListBinding>(),
    Observer<RepairListViewModel.RepairListUiState> {

    @Inject
    lateinit var mViewModel: RepairListViewModel

    private val repairListAdapter: RepairListAdapter by lazy { RepairListAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    override val bindingInflater: (LayoutInflater) -> ActivityRepairListBinding
        get() = { ActivityRepairListBinding.inflate(layoutInflater) }

    override fun setupView() {
        mViewModel.uiState.observe(this, this)
        mViewModel.getRepairList(1)

        binding.headerRepairList.btnBack.setOnClickListener{ onBackPressed() }
        binding.headerRepairList.tvTitleHeader.text = getString(R.string.repair_list)

        setRepairListActions()
        setRepairListPagination()
    }

    override fun onChanged(state: RepairListViewModel.RepairListUiState?) {
        when (state) {
            is RepairListViewModel.RepairListUiState.RepairListLoaded -> {
                stopLoading()
                repairListAdapter.appendList(state.list)
            }
            is RepairListViewModel.RepairListUiState.InitialLoading -> {
                startInitialLoading()
            }
            is RepairListViewModel.RepairListUiState.PagingLoading -> {
                startPagingLoading()
            }
            is RepairListViewModel.RepairListUiState.FailedLoadRepairList -> {
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
            mViewModel.getRepairList(page)
        }
        paginator?.let { binding.rvRepairList.addOnScrollListener(it) }
    }

    private fun setRepairListActions() {
        with(binding.rvRepairList) {
            adapter = repairListAdapter
            setHasFixedSize(true)

            repairListAdapter.setOnClickData {
                if (it.uuid?.contains("fdt") == true){
                    val intent = Intent(this@RepairListActivity, FdtDetailActivity::class.java)
                    intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.uuid)
                    startActivity(intent)
                }
                if(it.uuid?.contains("fat") == true){
                    val intent = Intent(this@RepairListActivity, FatDetailActivity::class.java)
                    intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.uuid)
                    startActivity(intent)
                }
            }
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

}