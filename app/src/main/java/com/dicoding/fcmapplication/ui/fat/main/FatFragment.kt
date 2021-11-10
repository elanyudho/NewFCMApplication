package com.dicoding.fcmapplication.ui.fat.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentFatBinding
import com.dicoding.fcmapplication.ui.fat.main.adapter.FatGridAdapter
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.shashank.sony.fancytoastlib.FancyToast
import javax.inject.Inject

class FatFragment : BaseFragmentBinding<FragmentFatBinding>(), Observer<FatViewModel.FatUiState> {

    @Inject
    lateinit var mViewModel: FatViewModel

    private val fatGridAdapter: FatGridAdapter by lazy { FatGridAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFatBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentFatBinding.inflate(layoutInflater, viewGroup, b)
        }

    override fun setupView() {
        callOnceWhenCreated {
            mViewModel.uiState.observe(viewLifecycleOwner, this@FatFragment)
        }
    }

    override fun onChanged(state: FatViewModel.FatUiState?) {
        when (state) {
            is FatViewModel.FatUiState.FatLoaded -> {
                stopLoading()
                fatGridAdapter.appendList(state.list)
                setFatActions()
                setFatPagination()
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
            }
        }
    }

    private fun setFatPagination() {
        paginator = RecyclerViewPaginator(binding.rvFat.layoutManager as GridLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            mViewModel.getFatList(page)
        }
        paginator?.let { binding.rvFat.addOnScrollListener(it) }
    }

    private fun setFatActions() {
        with(binding.rvFat) {
            adapter = fatGridAdapter
            setHasFixedSize(true)

            fatGridAdapter.setOnClickData { Toast.makeText(requireContext(), it.fatName, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun startInitialLoading() {
        binding.rvFat.gone()
    }

    private fun stopLoading() {
        binding.rvFat.visible()
        binding.progressFat.gone()
    }

    private fun startPagingLoading() {
        binding.progressFat.visible()
    }

}