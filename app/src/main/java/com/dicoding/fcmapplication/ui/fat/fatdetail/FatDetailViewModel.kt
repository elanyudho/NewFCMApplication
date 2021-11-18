package com.dicoding.fcmapplication.ui.fat.fatdetail

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatDetailUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FatDetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fatDetailUseCase: GetFatDetailUseCase
) : BaseViewModel<FatDetailViewModel.FatDetailUiState>() {

    sealed class FatDetailUiState {
        data class LoadingFatDetail(val isLoading: Boolean) : FatDetailUiState()
        data class FatDetailLoaded(val data: FatDetail) : FatDetailUiState()
        data class FailedLoadFatDetail(val failure: Failure) : FatDetailUiState()
    }

    fun getFatDetail(uuid: String) {
        _uiState.value = FatDetailUiState.LoadingFatDetail(true)
        viewModelScope.launch(dispatcherProvider.io) {
            fatDetailUseCase.run(uuid)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatDetailUiState.FatDetailLoaded(it)
                        _uiState.value = FatDetailUiState.LoadingFatDetail(false)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatDetailUiState.FailedLoadFatDetail(it)
                        _uiState.value = FatDetailUiState.LoadingFatDetail(false)
                    }
                }
        }

    }
}