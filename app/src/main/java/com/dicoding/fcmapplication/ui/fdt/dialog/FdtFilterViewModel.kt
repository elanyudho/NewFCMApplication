package com.dicoding.fcmapplication.ui.fdt.dialog

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.domain.usecase.auth.GetRegionListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FdtFilterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val regionListUseCase: GetRegionListUseCase
) : BaseViewModel<FdtFilterViewModel.FdtFilterUiState>() {

    sealed class  FdtFilterUiState {
        object Loading : FdtFilterUiState()
        data class SuccessRegionLoaded(val region: List<Region>) : FdtFilterUiState()
        data class Failed(val failure: Failure) : FdtFilterUiState()
    }

    fun getRegionList() {
        _uiState.value = FdtFilterUiState.Loading
        viewModelScope.launch (dispatcherProvider.io) {
            regionListUseCase.run(UseCase.None)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtFilterUiState.Loading
                        _uiState.value = FdtFilterUiState.SuccessRegionLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtFilterUiState.Loading
                        _uiState.value = FdtFilterUiState.Failed(it)
                    }
                }
        }
    }
}