package com.dicoding.fcmapplication.ui.other.adddata.addfdt

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.domain.usecase.other.GetCoveredFatListUseCase
import com.dicoding.fcmapplication.domain.usecase.other.PostFdtDataUseCase
import com.dicoding.fcmapplication.domain.usecase.other.PutFdtDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddFdtViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val postFdtDataUseCase: PostFdtDataUseCase,
    private val coveredFatListUseCase: GetCoveredFatListUseCase,
    private val putFdtDataUseCase: PutFdtDataUseCase
) : BaseViewModel<AddFdtViewModel.AddFdtUiState>() {

    private var failure: Failure? = null
    private var errorCode = 0

    sealed class AddFdtUiState {
        object SuccessPostOrPutFdtData : AddFdtUiState()
        data class CoveredFatListLoaded(val list: List<FdtDetail.FatList>) : AddFdtViewModel.AddFdtUiState()
        data class SuccessUpdateCoveredFatList(val coveredFatList: List<FdtDetail.FatList>) : AddFdtUiState()
        data class FailedLoadedOrTransaction(val failure: Failure) : AddFdtUiState()
        object Loading : AddFdtUiState()
    }

    sealed class Event {
        data class UpdateCoveredFat(
            val coveredFatList: List<FdtDetail.FatList>) : Event()
    }

    fun postFdtData(postFDT: PostFDT) {
        _uiState.value = AddFdtUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            postFdtDataUseCase.run(postFDT)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFdtUiState.SuccessPostOrPutFdtData
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFdtUiState.FailedLoadedOrTransaction(it)
                    }
                }
        }
    }

    fun putFdtData(id: String, putFdtData: PostFDT) {
        _uiState.value = AddFdtUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            putFdtDataUseCase.run(PutFdtDataUseCase.PutFdtData(id, putFdtData))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFdtUiState.SuccessPostOrPutFdtData
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFdtUiState.FailedLoadedOrTransaction(it)
                    }
                }
        }
    }

    private suspend fun setCoveredFatList(query: String): Flow<FdtDetail.FatList> {
        val fdtToAddList = ArrayList<FdtDetail.FatList>()
        coveredFatListUseCase.run(query)
            .onSuccess {
                it.map { coveredFat ->
                    val data = FdtDetail.FatList(
                        fatId = coveredFat.fatId,
                        fatName = coveredFat.fatName,
                        fatIsService = coveredFat.fatIsService,
                        fatActivated = coveredFat.fatActivated,
                        total = coveredFat.total,
                        coreUsed = coveredFat.coreUsed,
                        fdt = coveredFat.fdt
                    )
                    fdtToAddList.add(data)
                }
                errorCode = 0
            }
            .onError {
                failure = it
                errorCode = 1
            }
        return fdtToAddList.asFlow()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getCoveredFatList(query: String) {
        _uiState.value = AddFdtUiState.Loading
        viewModelScope.launch(dispatcherProvider.main) {
            val fdtToAddList = ArrayList<FdtDetail.FatList>()
            setCoveredFatList(query)
                .flowOn(dispatcherProvider.io)
                .distinctUntilChanged()
                .collect { data ->
                    fdtToAddList.add(data)
                }
            if (errorCode == 0) {
                _uiState.value = AddFdtUiState.CoveredFatListLoaded(fdtToAddList)
            } else {
                _uiState.value =
                    failure?.let { AddFdtUiState.FailedLoadedOrTransaction(it) }
            }
        }
    }

    fun doSomething(event: Event) {
        when(event) {

            is Event.UpdateCoveredFat -> {
                _uiState.value = AddFdtUiState.SuccessUpdateCoveredFatList(event.coveredFatList)
            }
        }
    }
}