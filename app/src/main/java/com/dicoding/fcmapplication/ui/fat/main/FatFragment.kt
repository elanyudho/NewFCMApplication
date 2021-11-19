package com.dicoding.fcmapplication.ui.fat.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.FragmentFatBinding
import com.dicoding.fcmapplication.ui.fat.adapter.FatGridAdapter
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
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

    private val fatGridAdapter: FatGridAdapter by lazy { FatGridAdapter() }

    private var paginator: RecyclerViewPaginator? = null

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

    override fun setupView() {
        callOnceWhenCreated {
            mViewModel.uiState.observe(viewLifecycleOwner, this@FatFragment)

            setFatActions()
            setFatPagination()
        }
        callOnceWhenDisplayed {
            mViewModel.getFatList(1) }
    }

    override fun onChanged(state: FatViewModel.FatUiState?) {
        when (state) {
            is FatViewModel.FatUiState.FatLoaded -> {
                stopLoading()
                fatGridAdapter.appendList(state.list)

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

            fatGridAdapter.setOnClickData { val intent = Intent(requireContext(), FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.uuid)
                startActivity(intent) }
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