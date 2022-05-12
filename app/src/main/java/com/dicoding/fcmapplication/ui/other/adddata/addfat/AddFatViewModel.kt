package com.dicoding.fcmapplication.ui.other.adddata.addfat

import androidx.lifecycle.viewModelScope
import com.dicoding.core.abstraction.BaseViewModel
import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.core.exception.Failure
import com.dicoding.core.extension.onError
import com.dicoding.core.extension.onSuccess
import com.dicoding.fcmapplication.domain.model.FdtToAdd
import com.dicoding.fcmapplication.domain.model.PostFAT
import com.dicoding.fcmapplication.domain.usecase.other.GetChooseFdtListUseCase
import com.dicoding.fcmapplication.domain.usecase.other.PostFatDataUseCase
import com.dicoding.fcmapplication.domain.usecase.other.PutFatDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddFatViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val postFatDataUseCase: PostFatDataUseCase,
    private val chooseFdtListUseCase: GetChooseFdtListUseCase,
    private val putFatDataUseCase: PutFatDataUseCase
) : BaseViewModel<AddFatViewModel.AddFatUiState>() {

    private var failure : Failure? = null
    private var errorCode = 0

    sealed class AddFatUiState {
        data class ChooseFdtListLoaded(val list: List<FdtToAdd>) : AddFatUiState()
        object SuccessPostOrPutFatData : AddFatUiState()
        data class FailedLoadedOrTransaction(val failure: Failure) : AddFatUiState()
        object Loading : AddFatUiState()
    }

    fun postFdtData(postFAT: PostFAT) {
        _uiState.value = AddFatUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            postFatDataUseCase.run(postFAT)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFatUiState.SuccessPostOrPutFatData
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFatUiState.FailedLoadedOrTransaction(it)
                    }
                }
        }
    }

    fun putFatData(id: String, putFatData: PostFAT) {
        _uiState.value = AddFatUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {
            putFatDataUseCase.run(PutFatDataUseCase.PutFatData(id, putFatData))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFatUiState.SuccessPostOrPutFatData
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddFatUiState.FailedLoadedOrTransaction(it)
                    }
                }
        }
    }

    private suspend fun setChooseFdtList(query: String) : Flow<FdtToAdd> {
        val fdtToAddList = ArrayList<FdtToAdd>()
        chooseFdtListUseCase.run(query)
            .onSuccess {
                it.map{ fdtToAdd ->
                    val data = FdtToAdd(
                        fdtId = fdtToAdd.fdtId,
                        fdtName = fdtToAdd.fdtName
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
    fun getChooseFdtList(query: String) {
        _uiState.value = AddFatUiState.Loading
        viewModelScope.launch (dispatcherProvider.main) {
            val fdtToAddList = ArrayList<FdtToAdd>()
            setChooseFdtList(query)
                .flowOn(dispatcherProvider.io)
                .distinctUntilChanged()
                .collect { data ->
                    fdtToAddList.add(data)
                }
            if(errorCode == 0){
                _uiState.value = AddFatUiState.ChooseFdtListLoaded(fdtToAddList)
            }else{
                _uiState.value = failure?.let { AddFatUiState.FailedLoadedOrTransaction(it) }
            }
        }
    }
}