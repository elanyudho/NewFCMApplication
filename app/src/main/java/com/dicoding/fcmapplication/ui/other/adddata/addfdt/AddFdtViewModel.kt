package com.dicoding.fcmapplication.ui.other.adddata.addfdt

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.domain.usecase.other.PostFdtDataUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddFdtViewModel @Inject constructor(
    private val  dispatcherProvider: DispatcherProvider,
    private val postFdtDataUseCase: PostFdtDataUseCase
) : BaseViewModel<AddFdtViewModel.AddFdtUiState>(){

    sealed class AddFdtUiState() {
        object SuccessPostFdtData: AddFdtUiState()
        data class FailedPostFdtData(val failure: Failure): AddFdtUiState()
        object LoadingPostFdtData: AddFdtUiState()
    }

    fun postFdtData(postFDT: PostFDT) {
        _uiState.value = AddFdtUiState.LoadingPostFdtData
        viewModelScope.launch (dispatcherProvider.io) {
            postFdtDataUseCase.run(postFDT)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFdtUiState.SuccessPostFdtData
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFdtUiState.FailedPostFdtData(it)
                    }
                }
        }
    }
}