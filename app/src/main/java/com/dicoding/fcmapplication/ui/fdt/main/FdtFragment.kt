package com.dicoding.fcmapplication.ui.fdt.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentFdtBinding
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtVerticalAdapter
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.ui.fdt.searchresult.SearchResultFdtActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FdtFragment : BaseFragmentBinding<FragmentFdtBinding>(),
    Observer<FdtViewModel.FdtUiState> {

    @Inject
    lateinit var mViewModel: FdtViewModel

    @Inject
    lateinit var session: Session

    private val fdtVerticalAdapter: FdtVerticalAdapter by lazy { FdtVerticalAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    private var refreshDataNotify: (() -> Unit)? = null

    private var resultLauncher : ActivityResultLauncher<Intent>? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFdtBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentFdtBinding.inflate(layoutInflater, viewGroup, b)
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
            mViewModel.uiState.observe(viewLifecycleOwner, this@FdtFragment)

            with(binding){
                tvFdtLocation.text = "FDT in ${session.user?.region}"
            }

            setResultLauncher()

            with(binding) {
                searchFdt.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(requireContext(), SearchResultFdtActivity::class.java)
                        intent.putExtra(SearchResultFdtActivity.EXTRA_NAME, query)
                        startActivity(intent)
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
            setFdtPagination()
        }
        callOnceWhenDisplayed {
            if (session.user?.isAdmin == true) {
                session.user?.region?.let { mViewModel.getFdtList(it, 1) }
            } else {
                session.user?.region?.let { mViewModel.getFdtList(it, 1) }
            }
        }
    }

    override fun onChanged(state: FdtViewModel.FdtUiState?) {
        when (state) {
            is FdtViewModel.FdtUiState.FdtLoaded -> {
                stopLoading()
                fdtVerticalAdapter.appendList(state.list)

            }
            is FdtViewModel.FdtUiState.InitialLoading -> {
               startInitialLoading()
            }
            is FdtViewModel.FdtUiState.PagingLoading -> {
                startPagingLoading()
            }
            is FdtViewModel.FdtUiState.FailedLoadFdt -> {
                requireActivity().fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
                //binding.progressFdt.gone()
            }
        }
    }

    fun getFdtListFromOutside() {
        fdtVerticalAdapter.clearList()
        if (session.user?.isAdmin == true) {
            session.user?.region?.let { mViewModel.getFdtList(it, 1) }
        } else {
            session.user?.region?.let { mViewModel.getFdtList(it, 1) }
        }
    }

    private fun setFdtPagination() {
        paginator = RecyclerViewPaginator(binding.rvFdt.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            // TODO: 14/02/2022 add condition for central admin
            session.user?.region?.let { mViewModel.getFdtList(it, page) }
        }
        paginator?.let { binding.rvFdt.addOnScrollListener(it) }
    }

    private fun setFdtActions() {
        with(binding.rvFdt) {
            adapter = fdtVerticalAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(5)

            fdtVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FdtDetailActivity::class.java)
                intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.fdtName)
                resultLauncher?.launch(intent)
            }
        }
    }

    fun setOnRefreshData(action: () -> Unit) {
        this.refreshDataNotify = action
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