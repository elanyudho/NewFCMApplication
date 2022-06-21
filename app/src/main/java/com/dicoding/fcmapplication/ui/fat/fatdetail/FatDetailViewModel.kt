package com.dicoding.fcmapplication.ui.fat.fatdetail

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.domain.usecase.fat.DeleteFatDataUseCase
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatDetailUseCase
import com.dicoding.fcmapplication.ui.fdt.fdtdetail.FdtDetailViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FatDetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val fatDetailUseCase: GetFatDetailUseCase,
    private val deleteFatDataUseCase: DeleteFatDataUseCase
) : BaseViewModel<FatDetailViewModel.FatDetailUiState>() {

    sealed class FatDetailUiState {
        data class Loading(val isLoading: Boolean) : FatDetailUiState()
        data class FatDetailLoaded(val data: FatDetail) : FatDetailUiState()
        object SuccessDeleteFat :FatDetailUiState()
        data class Failed(val failure: Failure) : FatDetailUiState()
    }

    fun getFatDetail(uuid: String) {
        _uiState.value = FatDetailUiState.Loading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            fatDetailUseCase.run(uuid)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatDetailUiState.FatDetailLoaded(it)
                        _uiState.value = FatDetailUiState.Loading(false)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatDetailUiState.Failed(it)
                        _uiState.value = FatDetailUiState.Loading(false)
                    }
                }
        }

    }

    fun deleteFat(uuid: String) {
        _uiState.value = FatDetailUiState.Loading(true)
        viewModelScope.launch(dispatcherProvider.io)  {
            deleteFatDataUseCase.run(uuid)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatDetailUiState.Loading(false)
                        _uiState.value = FatDetailUiState.SuccessDeleteFat
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatDetailUiState.Loading(false)
                        _uiState.value = FatDetailUiState.Failed(it)
                    }
                }
        }
    }
}