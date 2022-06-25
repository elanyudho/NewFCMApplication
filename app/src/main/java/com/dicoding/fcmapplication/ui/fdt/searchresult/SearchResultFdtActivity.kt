package com.dicoding.fcmapplication.ui.fdt.searchresult

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivitySearchResultFdtBinding
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtVerticalAdapter
import com.dicoding.fcmapplication.ui.fdt.dialog.FdtFilterDialogFragment
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

    private lateinit var filter: FdtFilterDialogFragment.Filter

    private var paginator: RecyclerViewPaginator? = null

    private var resultLauncher : ActivityResultLauncher<Intent>? = null

    private val searchFdtAdapter: FdtVerticalAdapter by lazy { FdtVerticalAdapter() }

    private var isFirstGet = true

    override val bindingInflater: (LayoutInflater) -> ActivitySearchResultFdtBinding
        get() = { ActivitySearchResultFdtBinding.inflate(layoutInflater) }

    override fun setupView() {
        filter = intent.getParcelableExtra(EXTRA_FILTER)!!

        mViewModel.uiState.observe(this, this)

        setFdtPagination()

        setFilterButton()

        setResultLauncher()

        if (session.user?.isCenterAdmin == true){
            mViewModel.getFdtSearchResult(filter.region, filter.search, 1)
        }else{
            mViewModel.getFdtSearchResult(session.user?.region.toString(), filter.search, 1)
        }

        binding.searchFdt.setQuery(filter.search)

        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            searchFdt.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    filter.search = query?: ""
                    isFirstGet = true
                    filter.region = ""

                    if (session.user?.isCenterAdmin == true){
                        mViewModel.getFdtSearchResult(filter.region, filter.search, 1)
                    }else{
                        mViewModel.getFdtSearchResult(session.user?.region.toString(), filter.search, 1)
                    }

                    searchFdtAdapter.clearList()
                    searchFdt.setQuery(filter.search)
                    searchFdt.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
            searchFdt.setOnAdditionalButtonListener {
                filter.search = searchFdt.getQuery()
                FdtFilterDialogFragment.build(
                    filter
                ){
                    isFirstGet = true
                    searchFdtAdapter.clearList()
                    searchFdt.setQuery(filter.search)
                    mViewModel.getFdtSearchResult(filter.region, filter.search, 1)
                    searchFdt.clearFocus()
                }.show(supportFragmentManager, FdtFilterDialogFragment::class.java.simpleName)
            }
        }

        setFdtActions()

    }

    override fun onChanged(state: SearchResultFdtViewModel.SearchResultFdtUiState?) {
        when (state) {
            is SearchResultFdtViewModel.SearchResultFdtUiState.SearchResultFdtLoaded -> {
                stopLoading()

                if(state.data.isEmpty() && isFirstGet){
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
                resultLauncher?.launch(intent)
            }
        }
    }

    private fun setFdtPagination() {
        paginator = RecyclerViewPaginator(binding.rvFdt.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            if (session.user?.isCenterAdmin == true){
                mViewModel.getFdtSearchResult(filter.region, filter.search, page)
            }else{
                mViewModel.getFdtSearchResult(session.user?.region.toString(), filter.search, page)
            }
            isFirstGet = false
        }
        paginator?.let { binding.rvFdt.addOnScrollListener(it) }
    }

    private fun setFilterButton() {
        binding.searchFdt.setAdditionalButtonImage(R.drawable.ic_filter)
        if (session.user?.isCenterAdmin == true){
            binding.searchFdt.isUsingAdditionalButton(true)
        }else{
            binding.searchFdt.isUsingAdditionalButton(false)
        }
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                searchFdtAdapter.clearList()
                mViewModel.getFdtSearchResult(session.user?.region.toString(), filter.search, 1)
                setResult(Activity.RESULT_OK)
            }
        }
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
        binding.progressFdt.gone()
    }

    private fun startPagingLoading() {
        binding.progressFdt.visible()
    }

    companion object {

        const val EXTRA_FILTER = "filter"

    }

}