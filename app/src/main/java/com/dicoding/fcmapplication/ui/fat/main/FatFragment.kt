package com.dicoding.fcmapplication.ui.fat.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentFatBinding
import com.dicoding.fcmapplication.ui.fat.adapter.FatVerticalAdapter
import com.dicoding.fcmapplication.ui.fat.dialog.FatFilterDialogFragment
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fat.searchresult.SearchResultFatActivity
import com.dicoding.fcmapplication.ui.fdt.dialog.FdtFilterDialogFragment
import com.dicoding.fcmapplication.ui.fdt.searchresult.SearchResultFdtActivity
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.setStatusBar
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FatFragment : BaseFragmentBinding<FragmentFatBinding>(), Observer<FatViewModel.FatUiState> {

    @Inject
    lateinit var mViewModel: FatViewModel

    @Inject
    lateinit var session: Session

    private val fatVerticalAdapter: FatVerticalAdapter by lazy { FatVerticalAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    private var refreshDataNotify: (() -> Unit)? = null

    private var resultLauncher : ActivityResultLauncher<Intent>? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFatBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentFatBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireContext().setStatusBar(R.color.blue_dim, activity)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun setupView() {
        callOnceWhenCreated {
            mViewModel.uiState.observe(viewLifecycleOwner, this@FatFragment)

            with(binding){
                tvFatLocation.text = "FAT in ${session.user?.region}"
            }

            setFilterButton()

            setResultLauncher()

            with(binding) {
                searchFat.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(requireContext(), SearchResultFatActivity::class.java)
                        intent.putExtra(SearchResultFatActivity.EXTRA_FILTER, FatFilterDialogFragment.Filter(search = query?: ""))
                        startActivity(intent)
                        searchFat.setQuery("")
                        searchFat.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }

                })
                searchFat.setOnAdditionalButtonListener {
                    val filter = FatFilterDialogFragment.Filter()
                    filter.search = searchFat.getQuery()
                    FatFilterDialogFragment.build(
                        filter = filter
                    ){
                        val intent = Intent(requireActivity(), SearchResultFatActivity::class.java).apply {
                            putExtra(SearchResultFatActivity.EXTRA_FILTER, it)
                        }
                        startActivity(intent)
                    }.show(childFragmentManager, this.javaClass.simpleName)
                }
            }

            setFatActions()
            setFatPagination()
        }
        callOnceWhenDisplayed {
            if (session.user?.isAdmin == true) {
                session.user?.region?.let { mViewModel.getFatList(it, 1) }
            } else {
                session.user?.region?.let { mViewModel.getFatList(it, 1) }
            }
        }
    }

    override fun onChanged(state: FatViewModel.FatUiState?) {
        when (state) {
            is FatViewModel.FatUiState.FatLoaded -> {
                stopLoading()

                fatVerticalAdapter.appendList(state.list)

            }
            is FatViewModel.FatUiState.InitialLoading -> {
                startInitialLoading()
            }
            is FatViewModel.FatUiState.PagingLoading -> {
                startPagingLoading()
            }
            is FatViewModel.FatUiState.FailedLoadFat -> {
                requireActivity().fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
                binding.progressFat.gone()
            }
        }
    }

    fun getFatListFromOutside() {
        fatVerticalAdapter.clearList()
        if (session.user?.isAdmin == true) {
            session.user?.region?.let { mViewModel.getFatList(it, 1) }
        } else {
            session.user?.region?.let { mViewModel.getFatList(it, 1) }
        }
    }

    private fun setFatPagination() {
        paginator = RecyclerViewPaginator(binding.rvFat.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            // TODO: 14/02/2022 Add Condition for central admin
            session.user?.region?.let { mViewModel.getFatList(it, page) }
        }
        paginator?.let { binding.rvFat.addOnScrollListener(it) }
    }

    private fun setFatActions() {
        with(binding.rvFat) {
            adapter = fatVerticalAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(5)

            fatVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                resultLauncher?.launch(intent)
            }
        }
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshDataNotify?.invoke()
            }
        }
    }

    private fun setFilterButton() {
        binding.searchFat.setAdditionalButtonImage(R.drawable.ic_filter)
        if (session.user?.isCenterAdmin == true){
            binding.searchFat.isUsingAdditionalButton(true)
        }else{
            binding.searchFat.isUsingAdditionalButton(false)
        }
    }

    fun setOnRefreshData(action: () -> Unit) {
        this.refreshDataNotify = action
    }

    private fun startInitialLoading() {
        binding.progressFat.visible()
    }

    private fun stopLoading() {
        binding.progressFat.gone()
    }

    private fun startPagingLoading() {
        binding.progressFat.visible()
    }

}