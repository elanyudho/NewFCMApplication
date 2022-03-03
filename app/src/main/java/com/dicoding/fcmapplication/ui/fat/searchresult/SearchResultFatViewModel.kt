package com.dicoding.fcmapplication.ui.fat.searchresult

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatSearchResultUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtSearchResultUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchResultFatViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getFatSearchResultUseCase: GetFatSearchResultUseCase
) : BaseViewModel<SearchResultFatViewModel.SearchResultFatUiState>() {

    sealed class SearchResultFatUiState {
        object InitialLoading : SearchResultFatUiState()
        object PagingLoading : SearchResultFatUiState()
        data class SearchResultFatLoaded(val data: List<Fat>) : SearchResultFatUiState()
        data class FailedLoadSearchResultFat(val failure: Failure) : SearchResultFatUiState()
    }

    fun getFatSearchResult(zone: String, fatName: String, page: Long) {
        _uiState.value = if (page == 1L) {
            SearchResultFatUiState.InitialLoading
        } else {
            SearchResultFatUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            getFatSearchResultUseCase.run(GetFatSearchResultUseCase.Params(zone, fatName, page))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = SearchResultFatUiState.SearchResultFatLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = SearchResultFatUiState.FailedLoadSearchResultFat(it)
                    }
                }
        }
    }
}