package com.dicoding.fcmapplication.ui.register

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.domain.usecase.auth.GetLoginUseCase
import com.dicoding.fcmapplication.domain.usecase.auth.GetRegisterUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val registerUseCase: GetRegisterUseCase
) : BaseViewModel<RegisterViewModel.RegisterUiState>(){

    sealed class RegisterUiState {
        object Loading : RegisterUiState()
        data class SuccessApi(val user: User) : RegisterUiState()
        data class Failed(val failure: Failure) : RegisterUiState()
    }

    fun doRegister(email: String, userName: String, password: String) {
        _uiState.value = RegisterUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {

            registerUseCase.run(GetRegisterUseCase.Params(email, userName, password, "false", "true", "false"))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RegisterUiState.SuccessApi(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RegisterUiState.Failed(it)
                    }
                }
        }
    }
}