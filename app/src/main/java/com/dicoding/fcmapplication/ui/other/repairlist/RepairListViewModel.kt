package com.dicoding.fcmapplication.ui.other.repairlist

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatListUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepairListViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fdtListUseCase: GetFdtListUseCase,
    private val fatListUseCase: GetFatListUseCase
) : BaseViewModel<RepairListViewModel.RepairListUiState>() {

    sealed class RepairListUiState {
        object InitialLoading: RepairListUiState()
        object PagingLoading: RepairListUiState()
        data class FdtLoaded(val list: List<Fdt>) : RepairListUiState()
        data class FatLoaded(val list: List<Fat>) : RepairListUiState()
        data class FailedLoad(val failure: Failure) : RepairListUiState()
    }

    fun getFdtList(zone: String, page: Long) {
        _uiState.value = if (page == 1L) {
            RepairListUiState.InitialLoading
        }else{
            RepairListUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            fdtListUseCase.run(GetFdtListUseCase.Params(zone, page))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.FdtLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.FailedLoad(it)
                    }
                }
        }
    }

    fun getFatList(zone: String, page: Long) {
        _uiState.value = if (page == 1L) {
            RepairListUiState.InitialLoading
        }else{
            RepairListUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            fatListUseCase.run(GetFatListUseCase.Params(zone, page))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.FatLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.FailedLoad(it)
                    }
                }
        }
    }
}