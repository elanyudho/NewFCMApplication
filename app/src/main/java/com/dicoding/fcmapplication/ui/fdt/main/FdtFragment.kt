package com.dicoding.fcmapplication.ui.fdt.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.core.abstraction.BaseFragmentBinding
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.data.pref.Session
import com.dicoding.fcmapplication.databinding.FragmentFdtBinding
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.ui.fdt.adapter.FdtVerticalAdapter
import com.dicoding.fcmapplication.ui.fdt.dialog.FdtFilterDialogFragment
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailActivity
import com.dicoding.fcmapplication.ui.fdt.searchresult.SearchResultFdtActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.google.android.material.tabs.TabLayout
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

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private var regionName = ""

    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null

    private var listRegion = listOf<Region>()

    private var isFirstGet = true

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

            if (session.user?.isCenterAdmin == false) {
                binding.tvFdtLocation.text = "FDT in ${session.user?.region}"
            }

            setFilterButton()

            setResultLauncher()

            setRegionTabView()

            with(binding) {
                searchFdt.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(requireContext(), SearchResultFdtActivity::class.java)
                        intent.putExtra(
                            SearchResultFdtActivity.EXTRA_FILTER,
                            FdtFilterDialogFragment.Filter(search = query ?: "")
                        )
                        resultLauncher?.launch(intent)
                        searchFdt.setQuery("")
                        searchFdt.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }

                })
                searchFdt.setOnAdditionalButtonListener {
                    val filter = FdtFilterDialogFragment.Filter()
                    filter.search = searchFdt.getQuery()
                    FdtFilterDialogFragment.build(
                        filter = filter
                    ) {
                        val intent =
                            Intent(requireActivity(), SearchResultFdtActivity::class.java).apply {
                                putExtra(SearchResultFdtActivity.EXTRA_FILTER, it)
                            }
                        startActivity(intent)
                    }.show(childFragmentManager, this.javaClass.simpleName)
                }
            }

            setFdtActions()
            setFdtPagination()
        }
        callOnceWhenDisplayed {
            isFirstGet = true
            if (session.user?.isCenterAdmin == true) {
                mViewModel.getRegionList()
            } else {
                session.user?.region?.let { mViewModel.getFdtList(it, 1) }
            }
        }
    }

    override fun onChanged(state: FdtViewModel.FdtUiState?) {
        when (state) {
            is FdtViewModel.FdtUiState.FdtLoaded -> {
                stopLoading()

                if (state.list.isEmpty() && isFirstGet) {
                    emptyDataView()
                }else {
                    dataIsNotEmptyView()
                    fdtVerticalAdapter.appendList(state.list)
                }

            }
            is FdtViewModel.FdtUiState.RegionLoaded -> {
                listRegion = state.list
                regionName = state.list[0].regionName ?: ""

                setTabItems()
                setTabAction()

                //init tvFdtLocation
                with(binding) {
                    tvFdtLocation.text = "FDT in $regionName"
                }

                mViewModel.getFdtList(regionName, 1)
            }
            is FdtViewModel.FdtUiState.InitialLoading -> {
                startInitialLoading()
            }
            is FdtViewModel.FdtUiState.PagingLoading -> {
                startPagingLoading()
            }
            is FdtViewModel.FdtUiState.LoadingRegion -> {
                if (state.isLoading) {
                    startInitialLoading()
                }
            }
            is FdtViewModel.FdtUiState.FailedLoaded -> {
                stopLoading()
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
        }
    }

    private fun setFdtPagination() {
        paginator = RecyclerViewPaginator(binding.rvFdt.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            if (session.user?.isCenterAdmin == true) {
                mViewModel.getFdtList(regionName, page)
            } else {
                session.user?.region?.let { mViewModel.getFdtList(it, page) }
            }
            isFirstGet = false
        }
        paginator?.let { binding.rvFdt.addOnScrollListener(it) }
    }

    private fun setTabAction() {
        onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabPosition = tab?.position?.let { listRegion[it] }
                regionName = tabPosition?.regionName ?: ""

                with(binding) {
                    tvFdtLocation.text = "FDT in $regionName"
                }

                fdtVerticalAdapter.clearList()

                mViewModel.getFdtList(regionName, 1)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }

        onTabSelectedListener?.let {
            binding.tabLayoutCourse.addOnTabSelectedListener(it)
        }
    }

    private fun setTabItems() {
        with(binding.tabLayoutCourse) {
            val tabList = mutableListOf<String>()
            listRegion.forEach {
                tabList.add(it.regionName!!)
            }
            // Add tabs
            addTitleOnlyTabs(tabList)

            // Set margins
            setTabsMargin(0, 6.dp, 12.dp, 6.dp)
        }
    }

    private fun setFdtActions() {
        with(binding.rvFdt) {
            adapter = fdtVerticalAdapter
            setHasFixedSize(true)

            fdtVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FdtDetailActivity::class.java)
                intent.putExtra(FdtDetailActivity.EXTRA_DETAIL_FDT, it.fdtName)
                resultLauncher?.launch(intent)

                hideKeyboard(requireActivity())
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

    private fun setFilterButton() {
        binding.searchFdt.setAdditionalButtonImage(R.drawable.ic_filter)
        if (session.user?.isCenterAdmin == true) {
            binding.searchFdt.isUsingAdditionalButton(true)
        } else {
            binding.searchFdt.isUsingAdditionalButton(false)
        }
    }

    private fun setRegionTabView() {
        val tvFdtLocation = binding.tvFdtLocation.layoutParams as ConstraintLayout.LayoutParams

        if (session.user?.isCenterAdmin == true) {
            with(binding) {
                tabLayoutCourse.visible()
                tvFdtLocation.topToBottom = tabLayoutCourse.id
                tvFdtLocation.topMargin = 16.dp
            }
        } else {
            with(binding) {
                tabLayoutCourse.gone()
                tvFdtLocation.topToBottom = searchFdt.id
                tvFdtLocation.topMargin = 16.dp
            }
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun emptyDataView() {
        with(binding){
            grpEmptyData.visible()
            rvFdt.gone()
        }
    }

    private fun dataIsNotEmptyView() {
        with(binding){
            grpEmptyData.gone()
            rvFdt.visible()
        }
    }

}