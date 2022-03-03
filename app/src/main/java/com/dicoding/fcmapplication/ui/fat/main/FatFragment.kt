package com.dicoding.fcmapplication.ui.fat.main

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
import com.dicoding.fcmapplication.databinding.FragmentFatBinding
import com.dicoding.fcmapplication.ui.fat.adapter.FatVerticalAdapter
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fat.searchresult.SearchResultFatActivity
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

            with(binding) {
                searchFat.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(requireContext(), SearchResultFatActivity::class.java)
                        intent.putExtra(SearchResultFatActivity.EXTRA_NAME, query)
                        startActivity(intent)
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
            setFatPagination()
        }
        callOnceWhenDisplayed {
            if (session.user?.isCenterAdmin == true) {
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

                val allFatCoreTotalList = ArrayList<Int>()
                val allFatCoreUsedList = ArrayList<Int>()
                val allFatCoreBackupList = ArrayList<Int>()

                fatVerticalAdapter.appendList(state.list)

                state.list.map {
                    it.fatCore?.let { fdtTotal -> allFatCoreTotalList.add(fdtTotal.toInt()) }
                    it.fatCoreUsed?.let { fdtUsed -> allFatCoreUsedList.add(fdtUsed.toInt()) }
                    it.fatCoreRemaining?.let { fdtBackup -> allFatCoreBackupList.add(fdtBackup.toInt()) }
                }
                val sumFatCoreTotal = allFatCoreTotalList.sum()
                val sumFatCoreUsed = allFatCoreUsedList.sum()
                val sumFatCoreBackup = allFatCoreBackupList.sum()

                setArcProgressBar(
                    sumFatCoreTotal.toString(),
                    sumFatCoreUsed.toString(),
                    sumFatCoreBackup.toString()
                )

                fatVerticalAdapter.valueIndicator = sumFatCoreTotal
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

            fatVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setArcProgressBar(
        allFatCoreTotal: String,
        allFatCoreUsed: String,
        allFatCoreBackup: String
    ) {
        val percentValue = allFatCoreUsed.toDouble() / allFatCoreTotal.toDouble() * 100
        val percentValueInt = percentValue.toInt()
        val percentValueStr = percentValueInt.toString()
        with(binding) {
            semiCircleArcProgressBar.setPercent(percentValueInt)
            tvCoreTotal.text = allFatCoreTotal
            tvCoreUsed.text = allFatCoreUsed
            tvBackup.text = allFatCoreBackup
            tvCapacityPercentage.text = "$percentValueStr%"
        }

    }

    private fun startInitialLoading() {
        binding.rvFat.gone()
        binding.progressFat.visible()
    }

    private fun stopLoading() {
        binding.rvFat.visible()
        binding.progressFat.gone()
    }

    private fun startPagingLoading() {
        binding.progressFat.visible()
    }

}