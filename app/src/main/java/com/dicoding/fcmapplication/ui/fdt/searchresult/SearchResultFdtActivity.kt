package com.dicoding.fcmapplication.ui.fdt.searchresult

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivitySearchResultFdtBinding
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtVerticalAdapter
import com.dicoding.fcmapplication.ui.fdt.adapter.SearchFdtNoPaginationAdapter
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFdtActivity : BaseActivityBinding<ActivitySearchResultFdtBinding>(),
    Observer<SearchResultFdtViewModel.SearchResultFdtUiState> {

    @Inject
    lateinit var mViewModel: SearchResultFdtViewModel

    private lateinit var queryFdtName: String

    private val fdtNoPaginationAdapter: SearchFdtNoPaginationAdapter by lazy { SearchFdtNoPaginationAdapter() }

    override val bindingInflater: (LayoutInflater) -> ActivitySearchResultFdtBinding
        get() = { ActivitySearchResultFdtBinding.inflate(layoutInflater) }

    override fun setupView() {
        queryFdtName = intent.getStringExtra(EXTRA_NAME) ?: ""

        mViewModel.uiState.observe(this, this)
        mViewModel.getFdtSearchResult(queryFdtName)

        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            searchFdt.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mViewModel.getFdtSearchResult(query)
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
                if(state.data.isEmpty()){
                    emptyDataView()
                }else{
                    fdtNoPaginationAdapter.submitList(state.data)
                    dataIsNotEmptyView()
                }
            }
            is SearchResultFdtViewModel.SearchResultFdtUiState.LoadingSearchResultFdt -> {
                // TODO: 24/11/2021 add loading
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
            adapter = fdtNoPaginationAdapter
            setHasFixedSize(true)

            fdtNoPaginationAdapter.setOnClickData {
                val intent = Intent(this@SearchResultFdtActivity, FdtDetailActivity::class.java)
                intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.uuid)
                startActivity(intent)
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
        with(binding){tvNotFound.gone()
            tvNotFound2.gone()
            imageNotFound.gone()
            rvFdt.visible()
        }
    }

    companion object {

        const val EXTRA_NAME = "device name"

    }

}