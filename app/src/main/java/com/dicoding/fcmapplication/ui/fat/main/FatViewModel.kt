package com.dicoding.fcmapplication.ui.fat.main

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatListUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FatViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fatListUseCase: GetFatListUseCase
) : BaseViewModel<FatViewModel.FatUiState>() {

    sealed class FatUiState {
        object InitialLoading: FatUiState()
        object PagingLoading: FatUiState()
        data class FatLoaded(val list: List<Fat>) : FatUiState()
        data class FailedLoadFat(val failure: Failure) : FatUiState()
    }

    fun getFatList(zone: String, page: Long) {
        _uiState.value = if (page == 1L) {
            FatUiState.InitialLoading
        }else{
            FatUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            fatListUseCase.run(GetFatListUseCase.Params(zone, page))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatUiState.FatLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatUiState.FailedLoadFat(it)
                    }
                }
        }
    }
}