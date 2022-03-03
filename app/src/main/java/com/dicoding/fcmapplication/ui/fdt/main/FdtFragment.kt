package com.dicoding.fcmapplication.ui.fdt.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    @Inject
    lateinit var session: Session

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

    @SuppressLint("SetTextI18n")
    override fun setupView() {
        callOnceWhenCreated {
            mViewModel.uiState.observe(viewLifecycleOwner, this@FdtFragment)

            setFdtActions()
            setFdtPagination()

            with(binding){
                tvArcBarLocationName.text = "Core FAT are used in ${session.user?.region}"
                tvFdtLocation.text = "FDT in ${session.user?.region}"
            }

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

                val allFdtCoreTotalList = ArrayList<Int>()
                val allFdtCoreUsedList = ArrayList<Int>()
                val allFdtCoreBackupList = ArrayList<Int>()

                fdtVerticalAdapter.appendList(state.list)

                state.list.map {
                    it.fdtCore?.let { fdtTotal -> allFdtCoreTotalList.add(fdtTotal.toInt()) }
                    it.fdtCoreUsed?.let { fdtUsed -> allFdtCoreUsedList.add(fdtUsed.toInt()) }
                    it.fdtCoreRemaining?.let { fdtBackup -> allFdtCoreBackupList.add(fdtBackup.toInt()) }
                }

                val sumFdtCoreTotal = allFdtCoreTotalList.sum()
                val sumFdtCoreUsed = allFdtCoreUsedList.sum()
                val sumFdtCoreBackup = allFdtCoreBackupList.sum()

                setArcProgressBar(
                    sumFdtCoreTotal.toString(),
                    sumFdtCoreUsed.toString(),
                    sumFdtCoreBackup.toString()
                )

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
            // TODO: 14/02/2022 add condition for central admin
            session.user?.region?.let { mViewModel.getFdtList(it, page) }
        }
        paginator?.let { binding.rvFdt.addOnScrollListener(it) }
    }

    private fun setFdtActions() {
        with(binding.rvFdt) {
            adapter = fdtVerticalAdapter
            setHasFixedSize(true)

            fdtVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FdtDetailActivity::class.java)
                intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.fdtName)
                startActivity(intent)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setArcProgressBar(
        allFdtCoreTotal: String,
        allFdtCoreUsed: String,
        allFdtCoreBackup: String
    ) {
        val percentValue = allFdtCoreUsed.toDouble() / allFdtCoreTotal.toDouble() * 100
        val percentValueInt = percentValue.toInt()
        val percentValueStr = percentValueInt.toString()
        with(binding) {
            semiCircleArcProgressBar.setPercent(percentValueInt)
            tvCoreTotal.text = allFdtCoreTotal
            tvCoreUsed.text = allFdtCoreUsed
            tvBackup.text = allFdtCoreBackup
            tvCapacityPercentage.text = "$percentValueStr%"
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