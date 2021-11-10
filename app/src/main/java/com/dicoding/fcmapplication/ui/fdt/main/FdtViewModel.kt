package com.dicoding.fcmapplication.ui.fdt.main

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
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
        object InitialLoading: FdtUiState()
        object PagingLoading: FdtUiState()
        data class FdtLoaded(val list: List<Fdt>) : FdtUiState()
        data class FailedLoadFdt(val failure: Failure) : FdtUiState()
    }

    fun getFdtList(page: Long) {
        _uiState.value = if (page == 1L) {
            FdtUiState.InitialLoading
        }else{
            FdtUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            fdtListUseCase.run(page)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtUiState.FdtLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FdtUiState.FailedLoadFdt(it)
                    }
                }
        }
    }
}