package com.dicoding.fcmapplication.ui.other.companyprofile

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.CompanyProfile
import com.dicoding.fcmapplication.domain.usecase.other.GetCompanyProfileUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CompanyProfileViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getCompanyProfileUseCase: GetCompanyProfileUseCase
) : BaseViewModel<CompanyProfileViewModel.CompanyProfileUiState>(){

    sealed class CompanyProfileUiState {
        data class LoadingCompanyProfile(val isLoading: Boolean) : CompanyProfileUiState()
        data class CompanyProfileLoaded(val data: CompanyProfile) : CompanyProfileUiState()
        data class FailedLoadCompanyProfile(val failure: Failure) : CompanyProfileUiState()
    }

    fun getCompanyProfile(){
        _uiState.value = CompanyProfileUiState.LoadingCompanyProfile(true)
        viewModelScope.launch(dispatcherProvider.io) {
            getCompanyProfileUseCase.run(UseCase.None)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = CompanyProfileUiState.LoadingCompanyProfile(false)
                        _uiState.value = CompanyProfileUiState.CompanyProfileLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = CompanyProfileUiState.LoadingCompanyProfile(false)
                        _uiState.value = CompanyProfileUiState.FailedLoadCompanyProfile(it)
                    }
                }
        }
    }
}