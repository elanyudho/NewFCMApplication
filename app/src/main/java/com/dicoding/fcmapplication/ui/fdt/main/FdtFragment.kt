package com.dicoding.fcmapplication.ui.fdt.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentFdtBinding
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtGridAdapter
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtVerticalAdapter
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.setStatusBar
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FdtFragment : BaseFragmentBinding<FragmentFdtBinding>(),
    Observer<FdtViewModel.FdtUiState> {

    @Inject
    lateinit var mViewModel: FdtViewModel

    private val fdtVerticalAdapter: FdtVerticalAdapter by lazy { FdtVerticalAdapter() }

    private var paginator: RecyclerViewPaginator? = null

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

    override fun setupView() {
        callOnceWhenCreated {
            mViewModel.uiState.observe(viewLifecycleOwner, this@FdtFragment)

            setFdtActions()
            setFdtPagination()
        }

        callOnceWhenDisplayed {
            mViewModel.getFdtList(1)
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
            }
        }
    }

    private fun setFdtPagination() {
        paginator = RecyclerViewPaginator(binding.rvFdt.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            mViewModel.getFdtList(page)
        }
        paginator?.let { binding.rvFdt.addOnScrollListener(it) }
    }

    private fun setFdtActions() {
        with(binding.rvFdt) {
            adapter = fdtVerticalAdapter
            setHasFixedSize(true)

            fdtVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FdtDetailActivity::class.java)
                intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.uuid)
                startActivity(intent)
            }
        }
    }

    private fun startInitialLoading() {
        binding.rvFdt.gone()
        binding.progressFdt.visible()
    }

    private fun stopLoading() {
        binding.rvFdt.visible()
        binding.progressFdt.gone()
    }

    private fun startPagingLoading() {
        binding.progressFdt.visible()
    }

}