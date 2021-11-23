package com.dicoding.fcmapplication.ui.other.repairlist

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.domain.usecase.other.GetRepairListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepairListViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val repairListUseCase: GetRepairListUseCase
) : BaseViewModel<RepairListViewModel.RepairListUiState>() {

    sealed class RepairListUiState {
        object InitialLoading: RepairListUiState()
        object PagingLoading: RepairListUiState()
        data class RepairListLoaded(val list: List<Repair>) : RepairListUiState()
        data class FailedLoadRepairList(val failure: Failure) : RepairListUiState()
    }

    fun getRepairList(page: Long) {
        _uiState.value = if (page == 1L) {
            RepairListUiState.InitialLoading
        }else{
            RepairListUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            repairListUseCase.run(page)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.RepairListLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RepairListUiState.FailedLoadRepairList(it)
                    }
                }
        }
    }
}