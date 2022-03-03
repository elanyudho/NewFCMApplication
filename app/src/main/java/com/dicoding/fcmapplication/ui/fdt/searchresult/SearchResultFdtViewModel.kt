package com.dicoding.fcmapplication.ui.fdt.searchresult

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtSearchResultUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchResultFdtViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getFdtSearchResultUseCase: GetFdtSearchResultUseCase
) : BaseViewModel<SearchResultFdtViewModel.SearchResultFdtUiState>() {

    sealed class SearchResultFdtUiState {
        object InitialLoading: SearchResultFdtViewModel.SearchResultFdtUiState()
        object PagingLoading: SearchResultFdtViewModel.SearchResultFdtUiState()
        data class SearchResultFdtLoaded(val data: List<Fdt>) : SearchResultFdtUiState()
        data class FailedLoadSearchResultFdt(val failure: Failure) : SearchResultFdtUiState()
    }

    fun getFdtSearchResult(zone: String, fdtName: String, page: Long) {
        _uiState.value = if (page == 1L) {
            SearchResultFdtUiState.InitialLoading
        }else{
            SearchResultFdtUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            getFdtSearchResultUseCase.run(GetFdtSearchResultUseCase.Params(zone, fdtName, page))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = SearchResultFdtUiState.SearchResultFdtLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = SearchResultFdtUiState.FailedLoadSearchResultFdt(it)
                    }
                }
        }
    }
}