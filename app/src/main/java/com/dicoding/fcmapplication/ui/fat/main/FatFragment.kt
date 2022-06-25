package com.dicoding.fcmapplication.ui.fat.main

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
import com.dicoding.fcmapplication.databinding.FragmentFatBinding
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.ui.fat.adapter.FatVerticalAdapter
import com.dicoding.fcmapplication.ui.fat.dialog.FatFilterDialogFragment
import com.dicoding.fcmapplication.ui.fat.fatdetail.FatDetailActivity
import com.dicoding.fcmapplication.ui.fat.searchresult.SearchResultFatActivity
import com.dicoding.fcmapplication.utils.extensions.*
import com.dicoding.fcmapplication.utils.pagination.RecyclerViewPaginator
import com.google.android.material.tabs.TabLayout
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

    private var regionName = ""

    private var listRegion = listOf<Region>()

    private var isFirstGet = true

    private var onTabSelectedListener: TabLayout.OnTabSelectedListener? = null

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

            if (session.user?.isCenterAdmin == false){
                with(binding){
                    tvFatLocation.text = "FAT in ${session.user?.region}"
                }
            }

            setFilterButton()

            setResultLauncher()

            setRegionTabView()

            with(binding) {
                searchFat.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(requireContext(), SearchResultFatActivity::class.java)
                        intent.putExtra(SearchResultFatActivity.EXTRA_FILTER, FatFilterDialogFragment.Filter(search = query?: ""))
                        resultLauncher?.launch(intent)
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
            isFirstGet = true
            if (session.user?.isCenterAdmin == true) {
                mViewModel.getRegionList()
            } else {
                session.user?.region?.let { mViewModel.getFatList(it, 1) }
            }
        }
    }

    override fun onChanged(state: FatViewModel.FatUiState?) {
        when (state) {
            is FatViewModel.FatUiState.FatLoaded -> {
                stopLoading()

                if (state.list.isEmpty() && isFirstGet) {
                    emptyDataView()
                }else {
                    dataIsNotEmptyView()
                    fatVerticalAdapter.appendList(state.list)
                }

            }
            is FatViewModel.FatUiState.RegionLoaded -> {
                listRegion = state.list
                regionName = state.list[0].regionName ?: ""

                setTabItems()
                setTabAction()

                //init tvFatLocation
                with(binding) {
                    tvFatLocation.text = "FAT in $regionName"
                }

                mViewModel.getFatList(regionName, 1)
            }
            is FatViewModel.FatUiState.InitialLoading -> {
                startInitialLoading()
            }
            is FatViewModel.FatUiState.PagingLoading -> {
                startPagingLoading()
            }
            is FatViewModel.FatUiState.LoadingRegion -> {
                if (state.isLoading) {
                    startInitialLoading()
                }
            }
            is FatViewModel.FatUiState.FailedLoad -> {
                requireActivity().fancyToast(
                    getString(R.string.error_unknown_error),
                    FancyToast.ERROR
                )
                stopLoading()
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
            if (session.user?.isCenterAdmin == true) {
                mViewModel.getFatList(regionName, page)
            } else {
                session.user?.region?.let { mViewModel.getFatList(it, page) }
            }
            isFirstGet = false
        }
        paginator?.let { binding.rvFat.addOnScrollListener(it) }
    }

    private fun setTabAction() {
        onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabPosition = tab?.position?.let { listRegion[it] }
                regionName = tabPosition?.regionName ?: ""

                with(binding) {
                    tvFatLocation.text = "FAT in $regionName"
                }

                fatVerticalAdapter.clearList()
                mViewModel.getFatList(regionName, 1)
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

    private fun setFatActions() {
        with(binding.rvFat) {
            adapter = fatVerticalAdapter
            setHasFixedSize(true)

            fatVerticalAdapter.setOnClickData {
                val intent = Intent(requireContext(), FatDetailActivity::class.java)
                intent.putExtra(FatDetailActivity.EXTRA_DETAIL_FAT, it.fatName)
                resultLauncher?.launch(intent)

                hideKeyboard(requireActivity())
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

    private fun setRegionTabView() {
        val tvFatLocation = binding.tvFatLocation.layoutParams as ConstraintLayout.LayoutParams

        if (session.user?.isCenterAdmin == true) {
            with(binding) {
                tabLayoutCourse.visible()
                tvFatLocation.topToBottom = tabLayoutCourse.id
                tvFatLocation.topMargin = 16.dp
            }
        } else {
            with(binding) {
                tabLayoutCourse.gone()
                tvFatLocation.topToBottom = searchFat.id
                tvFatLocation.topMargin = 16.dp
            }
        }
    }

    fun setOnRefreshData(action: () -> Unit) {
        this.refreshDataNotify = action
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
        binding.progressFat.visible()
    }

    private fun stopLoading() {
        binding.progressFat.gone()
    }

    private fun startPagingLoading() {
        binding.progressFat.visible()
    }

    private fun emptyDataView() {
        with(binding){
            grpEmptyData.visible()
            rvFat.gone()
        }
    }

    private fun dataIsNotEmptyView() {
        with(binding){
            grpEmptyData.gone()
            rvFat.visible()
        }
    }

}