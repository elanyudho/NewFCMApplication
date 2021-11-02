package com.dicoding.fcmapplication.ui.login

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.domain.usecase.auth.GetLoginUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val loginUseCase: GetLoginUseCase
) : BaseViewModel<LoginViewModel.LoginUiState>(){

    sealed class LoginUiState {
        object Loading : LoginUiState()
        data class SuccessApi(val user: User) : LoginUiState()
        data class Failed(val failure: Failure) : LoginUiState()
    }

    fun doLogin(email: String, password: String) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {

            loginUseCase.run(GetLoginUseCase.Params(email, password))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = LoginUiState.SuccessApi(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = LoginUiState.Failed(it)
                    }
                }
        }
    }
}