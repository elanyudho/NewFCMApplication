package com.dicoding.fcmapplication.ui.other.regionadmin

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityRegionAdminBinding
import com.dicoding.fcmapplication.ui.other.adapter.RegionAdminAdapter
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class RegionAdminActivity : BaseActivityBinding<ActivityRegionAdminBinding>(),
    Observer<RegionAdminViewModel.RegionAdminUiState> {

    @Inject
    lateinit var mViewModel: RegionAdminViewModel

    private val regionAdminAdapter: RegionAdminAdapter by lazy { RegionAdminAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    override val bindingInflater: (LayoutInflater) -> ActivityRegionAdminBinding
        get() = { ActivityRegionAdminBinding.inflate(layoutInflater) }

    override fun setupView() {

        binding.headerRegionAdmin.tvTitleHeader.text = getString(R.string.region_admin)
        binding.headerRegionAdmin.btnBack.setOnClickListener { onBackPressed() }

        mViewModel.uiState.observe(this, this)
        mViewModel.getRegionAdminList(1)

        setRegionAdminActions()
        setFdtPagination()
    }

    override fun onChanged(state: RegionAdminViewModel.RegionAdminUiState?) {
        when (state) {
            is RegionAdminViewModel.RegionAdminUiState.RegionAdminLoaded -> {
                stopLoading()
                regionAdminAdapter.appendList(state.list)
            }
            is RegionAdminViewModel.RegionAdminUiState.InitialLoading -> {
                startInitialLoading()
            }
            is RegionAdminViewModel.RegionAdminUiState.PagingLoading -> {
                startPagingLoading()
            }
            is RegionAdminViewModel.RegionAdminUiState.FailedLoaded -> {
                stopLoading()
                fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
            }
        }
    }

    private fun setRegionAdminActions() {
        with(binding.rvRegionAdmin) {
            adapter = regionAdminAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(5)

        }
    }

    private fun setFdtPagination() {
        paginator = RecyclerViewPaginator(binding.rvRegionAdmin.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            mViewModel.getRegionAdminList(page)
        }
        paginator?.let { binding.rvRegionAdmin.addOnScrollListener(it) }
    }

    private fun startInitialLoading() {
        binding.progressFdt.visible()
    }

    private fun stopLoading() {
        binding.progressFdt.gone()
    }

    private fun startPagingLoading() {
        binding.progressFdt.visible()
    }
}