package com.dicoding.fcmapplication.ui.other.repairlist

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatListNoPageUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtListNoPageUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepairListViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fdtListNoPageUseCase: GetFdtListNoPageUseCase,
    private val fatListNoPageUseCase: GetFatListNoPageUseCase
) : BaseViewModel<RepairListViewModel.RepairListUiState>() {

    sealed class RepairListUiState {
        data class Loading(val isLoading: Boolean) : RepairListUiState()
        data class FdtLoaded(val list: List<Fdt>) : RepairListUiState()
        data class FatLoaded(val list: List<Fat>) : RepairListUiState()
        data class FailedLoad(val failure: Failure) : RepairListUiState()
    }

    fun getFdtList(zone: String) {
        _uiState.value = RepairListUiState.Loading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            fdtListNoPageUseCase.run(zone)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.Loading(false)
                        _uiState.value = RepairListUiState.FdtLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.Loading(true)
                        _uiState.value = RepairListUiState.FailedLoad(it)
                    }
                }
        }
    }

    fun getFatList(zone: String) {
        _uiState.value = RepairListUiState.Loading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            fatListNoPageUseCase.run(zone)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.Loading(false)
                        _uiState.value = RepairListUiState.FatLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.Loading(false)
                        _uiState.value = RepairListUiState.FailedLoad(it)
                    }
                }
        }
    }
}