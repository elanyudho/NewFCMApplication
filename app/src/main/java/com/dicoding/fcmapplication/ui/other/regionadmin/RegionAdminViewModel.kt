package com.dicoding.fcmapplication.ui.other.regionadmin

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.RegionAdmin
import com.dicoding.fcmapplication.domain.usecase.other.GetRegionAdminListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegionAdminViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val regionAdminListUseCase: GetRegionAdminListUseCase
) : BaseViewModel<RegionAdminViewModel.RegionAdminUiState>() {

    sealed class RegionAdminUiState {
        object InitialLoading: RegionAdminUiState()
        object PagingLoading: RegionAdminUiState()
        data class RegionAdminLoaded(val list: List<RegionAdmin>) : RegionAdminUiState()
        data class FailedLoaded(val failure: Failure) : RegionAdminUiState()
    }

    fun getRegionAdminList(page: Long) {
        _uiState.value = if (page == 1L) {
            RegionAdminUiState.InitialLoading
        }else{
            RegionAdminUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            regionAdminListUseCase.run(page)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RegionAdminUiState.RegionAdminLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RegionAdminUiState.FailedLoaded(it)
                    }
                }
        }
    }
}