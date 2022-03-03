package com.dicoding.fcmapplication.ui.fdt.searchresult

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivitySearchResultFdtBinding
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtVerticalAdapter
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFdtActivity : BaseActivityBinding<ActivitySearchResultFdtBinding>(),
    Observer<SearchResultFdtViewModel.SearchResultFdtUiState> {

    @Inject
    lateinit var mViewModel: SearchResultFdtViewModel

    @Inject
    lateinit var session: Session

    private lateinit var queryFdtName: String

    private var paginator: RecyclerViewPaginator? = null

    private val searchFdtAdapter: FdtVerticalAdapter by lazy { FdtVerticalAdapter() }

    override val bindingInflater: (LayoutInflater) -> ActivitySearchResultFdtBinding
        get() = { ActivitySearchResultFdtBinding.inflate(layoutInflater) }

    override fun setupView() {
        queryFdtName = intent.getStringExtra(EXTRA_NAME) ?: ""

        mViewModel.uiState.observe(this, this)

        setFdtPagination()

        if (session.user?.isCenterAdmin == true){
            mViewModel.getFdtSearchResult(session.user?.region.toString(), queryFdtName, 1)
        }else{
            mViewModel.getFdtSearchResult(session.user?.region.toString(), queryFdtName, 1)
        }

        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            searchFdt.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (session.user?.isCenterAdmin == true){
                        mViewModel.getFdtSearchResult(session.user?.region.toString(), queryFdtName, 1)
                    }else{
                        mViewModel.getFdtSearchResult(session.user?.region.toString(), queryFdtName, 1)
                    }
                    searchFdt.setQuery("")
                    searchFdt.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }

        setFdtActions()

    }

    override fun onChanged(state: SearchResultFdtViewModel.SearchResultFdtUiState?) {
        when (state) {
            is SearchResultFdtViewModel.SearchResultFdtUiState.SearchResultFdtLoaded -> {
                stopLoading()
                val allFdtCoreTotalList = ArrayList<Int>()

                if(state.data.isEmpty()){
                    emptyDataView()
                }else{
                    searchFdtAdapter.appendList(state.data)
                    dataIsNotEmptyView()
                }

            }
            is SearchResultFdtViewModel.SearchResultFdtUiState.InitialLoading -> {
                startInitialLoading()

            }
            is SearchResultFdtViewModel.SearchResultFdtUiState.PagingLoading -> {
                startPagingLoading()
            }
            is SearchResultFdtViewModel.SearchResultFdtUiState.FailedLoadSearchResultFdt -> {
                this.fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
            }
        }
    }

    private fun setFdtActions() {
        with(binding.rvFdt) {
            adapter = searchFdtAdapter
            setHasFixedSize(true)

            searchFdtAdapter.setOnClickData {
                val intent = Intent(this@SearchResultFdtActivity, FdtDetailActivity::class.java)
                intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.fdtName)
                startActivity(intent)
            }
        }
    }

    private fun setFdtPagination() {
        paginator = RecyclerViewPaginator(binding.rvFdt.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            // TODO: 14/02/2022 add condition for central admin
            if (session.user?.isCenterAdmin == true){
                mViewModel.getFdtSearchResult(session.user?.region.toString(), queryFdtName, page)
            }else{
                mViewModel.getFdtSearchResult(session.user?.region.toString(), queryFdtName, page)
            }
        }
        paginator?.let { binding.rvFdt.addOnScrollListener(it) }
    }

    private fun emptyDataView() {
        with(binding){
            tvNotFound.visible()
            tvNotFound2.visible()
            imageNotFound.visible()
            rvFdt.gone()
        }
    }

    private fun dataIsNotEmptyView() {
        with(binding){
            tvNotFound.gone()
            tvNotFound2.gone()
            imageNotFound.gone()
            rvFdt.visible()
        }
    }

    private fun startInitialLoading() {
        with(binding) {
            rvFdt.gone()
            tvNotFound.gone()
            tvNotFound2.gone()
            imageNotFound.gone()
            cvLottieLoading.visible()
        }
    }

    private fun stopLoading() {
        binding.cvLottieLoading.gone()
    }

    private fun startPagingLoading() {
        binding.progressFdt.visible()
    }

    companion object {

        const val EXTRA_NAME = "device name"

    }

}