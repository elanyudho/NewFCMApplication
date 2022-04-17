package com.dicoding.fcmapplication.ui.fat.searchresult

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.ActivitySearchResultFatBinding
import com.dicoding.fcmapplication.ui.fat.adapter.FatVerticalAdapter
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFatActivity : BaseActivityBinding<ActivitySearchResultFatBinding>(),
    Observer<SearchResultFatViewModel.SearchResultFatUiState> {

    @Inject
    lateinit var mViewModel: SearchResultFatViewModel

    @Inject
    lateinit var session: Session

    private lateinit var queryFatName: String

    private val searchFatAdapter: FatVerticalAdapter by lazy { FatVerticalAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    private var isFirstGet = true

    override val bindingInflater: (LayoutInflater) -> ActivitySearchResultFatBinding
        get() = { ActivitySearchResultFatBinding.inflate(layoutInflater) }

    override fun setupView() {
        queryFatName = intent.getStringExtra(EXTRA_NAME) ?: ""

        mViewModel.uiState.observe(this, this)
        if (session.user?.isCenterAdmin == true){
            mViewModel.getFatSearchResult(session.user?.region.toString(), queryFatName, 1)
        }else{
            mViewModel.getFatSearchResult(session.user?.region.toString(), queryFatName, 1)
        }

        binding.searchFat.setQuery(queryFatName)

        setFatPagination()

        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            searchFat.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (session.user?.isCenterAdmin == true){
                        // TODO: 20/02/2022 add logic for isCenterAdmin
                        if (query != null) {
                            mViewModel.getFatSearchResult(session.user?.region.toString(), query, 1)
                            queryFatName = query
                        }
                    }else{
                        if (query != null) {
                            mViewModel.getFatSearchResult(session.user?.region.toString(), query, 1)
                            queryFatName = query
                        }
                    }

                    searchFatAdapter.clearList()
                    searchFat.setQuery("")
                    searchFat.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }

        setFatActions()
    }

    companion object {

        const val EXTRA_NAME = "device name"

    }

    override fun onChanged(state: SearchResultFatViewModel.SearchResultFatUiState?) {
        when (state) {
            is SearchResultFatViewModel.SearchResultFatUiState.SearchResultFatLoaded -> {
                stopLoading()

                if (state.data.isEmpty() && isFirstGet) {
                    emptyDataView()
                } else {
                    searchFatAdapter.appendList(state.data)
                    dataIsNotEmptyView()
                }

            }
            is SearchResultFatViewModel.SearchResultFatUiState.InitialLoading -> {
                startInitialLoading()

            }
            is SearchResultFatViewModel.SearchResultFatUiState.PagingLoading -> {
                startPagingLoading()
            }
            is SearchResultFatViewModel.SearchResultFatUiState.FailedLoadSearchResultFat -> {
                this.fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
            }
        }
    }

    private fun setFatActions() {
        with(binding.rvFat) {
            adapter = searchFatAdapter
            setHasFixedSize(true)

            searchFatAdapter.setOnClickData {
                val intent = Intent(this@SearchResultFatActivity, FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                startActivity(intent)
            }
        }
    }

    private fun setFatPagination() {
        paginator = RecyclerViewPaginator(binding.rvFat.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            // TODO: 14/02/2022 add condition for central admin
            if (session.user?.isCenterAdmin == true){
                mViewModel.getFatSearchResult(session.user?.region.toString(), queryFatName, page)
            }else{
                mViewModel.getFatSearchResult(session.user?.region.toString(), queryFatName, page)
            }
            isFirstGet = false
        }
        paginator?.let { binding.rvFat.addOnScrollListener(it) }
    }

    private fun emptyDataView() {
        with(binding){
            tvNotFound.visible()
            tvNotFound2.visible()
            imageNotFound.visible()
            rvFat.gone()
        }
    }

    private fun dataIsNotEmptyView() {
        with(binding){
            tvNotFound.gone()
            tvNotFound2.gone()
            imageNotFound.gone()
            rvFat.visible()
        }
    }

    private fun startInitialLoading() {
        with(binding) {
            rvFat.gone()
            tvNotFound.gone()
            tvNotFound2.gone()
            imageNotFound.gone()
            cvLottieLoading.visible()
        }
    }

    private fun stopLoading() {
        binding.cvLottieLoading.gone()
        binding.progressFat.gone()
    }

    private fun startPagingLoading() {
        binding.progressFat.visible()
    }

}