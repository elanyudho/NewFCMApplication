package com.dicoding.fcmapplication.ui.fat.dialog

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

class FatFilterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val regionListUseCase: GetRegionListUseCase
) : BaseViewModel<FatFilterViewModel.FatFilterUiState>() {

    sealed class  FatFilterUiState {
        object Loading : FatFilterUiState()
        data class SuccessRegionLoaded(val region: List<Region>) : FatFilterUiState()
        data class Failed(val failure: Failure) : FatFilterUiState()
    }

    fun getRegionList() {
        _uiState.value = FatFilterUiState.Loading
        viewModelScope.launch (dispatcherProvider.io) {
            regionListUseCase.run(UseCase.None)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatFilterUiState.Loading
                        _uiState.value = FatFilterUiState.SuccessRegionLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = FatFilterUiState.Loading
                        _uiState.value = FatFilterUiState.Failed(it)
                    }
                }
        }
    }
}