package com.dicoding.fcmapplication.ui.fat.searchresult

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import com.dicoding.core.abstraction.BaseActivityBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivitySearchResultFatBinding
import com.dicoding.fcmapplication.ui.fat.adapter.SearchFatNoPaginationAdapter
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fdt.searchresult.SearchResultFdtActivity
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFatActivity : BaseActivityBinding<ActivitySearchResultFatBinding>(),
    Observer<SearchResultFatViewModel.SearchResultFatUiState> {

    @Inject
    lateinit var mViewModel: SearchResultFatViewModel

    private lateinit var queryFatName: String

    private val fatNoPaginationAdapter: SearchFatNoPaginationAdapter by lazy { SearchFatNoPaginationAdapter() }

    override val bindingInflater: (LayoutInflater) -> ActivitySearchResultFatBinding
        get() = { ActivitySearchResultFatBinding.inflate(layoutInflater) }

    override fun setupView() {
        queryFatName = intent.getStringExtra(EXTRA_NAME) ?: ""

        mViewModel.uiState.observe(this, this)
        mViewModel.getFatSearchResult(queryFatName)

        with(binding) {
            btnBack.setOnClickListener { onBackPressed() }
            searchFat.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mViewModel.getFatSearchResult(query)
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
                if(state.data.isEmpty()){
                    emptyDataView()
                }else{
                    fatNoPaginationAdapter.submitList(state.data)
                    dataIsNotEmptyView()
                }
            }
            is SearchResultFatViewModel.SearchResultFatUiState.LoadingSearchResultFat -> {
                // TODO: 24/11/2021 add loading
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
            adapter = fatNoPaginationAdapter
            setHasFixedSize(true)

            fatNoPaginationAdapter.setOnClickData { val intent = Intent(this@SearchResultFatActivity, FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.uuid)
                startActivity(intent) }
        }
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
        with(binding){tvNotFound.gone()
        tvNotFound2.gone()
        imageNotFound.gone()
        rvFat.visible()
        }
    }

}