package com.dicoding.fcmapplication.ui.fdt

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FdtViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fdtListUseCase: GetFdtListUseCase
) : BaseViewModel<FdtViewModel.FdtUiState>() {

    sealed class FdtUiState {
        data class LoadingFdt(val isLoading: Boolean) : FdtUiState()
        data class FdtLoaded(val list: List<Fdt>) : FdtUiState()
        data class FailedLoadFdt(val failure: Failure) : FdtUiState()
    }

    fun getFdtList() {
        _uiState.value = FdtUiState.LoadingFdt(true)
        viewModelScope.launch(dispatcherProvider.io) {
            fdtListUseCase.run(UseCase.None)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtUiState.FdtLoaded(it)
                        _uiState.value = FdtUiState.LoadingFdt(false)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtUiState.FailedLoadFdt(it)
                        _uiState.value = FdtUiState.LoadingFdt(false)
                    }
                }
        }
    }
}