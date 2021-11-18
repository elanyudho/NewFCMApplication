package com.dicoding.fcmapplication.ui.fdt.fdtdetail

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtDetailUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FdtDetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fdtDetailUseCase: GetFdtDetailUseCase
) : BaseViewModel<FdtDetailViewModel.FdtDetailUiState>() {

    sealed class FdtDetailUiState {
        data class LoadingFdtDetail(val isLoading: Boolean) : FdtDetailUiState()
        data class FdtDetailLoaded(val data: FdtDetail) : FdtDetailUiState()
        data class FailedLoadFdtDetail(val failure: Failure) : FdtDetailUiState()
    }

    fun getFdtDetail(uuid: String) {
        _uiState.value = FdtDetailUiState.LoadingFdtDetail(true)
        viewModelScope.launch(dispatcherProvider.io) {
            fdtDetailUseCase.run(uuid)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtDetailUiState.FdtDetailLoaded(it)
                        _uiState.value = FdtDetailUiState.LoadingFdtDetail(false)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtDetailUiState.FailedLoadFdtDetail(it)
                        _uiState.value = FdtDetailUiState.LoadingFdtDetail(false)
                    }
                }
        }

    }
}
