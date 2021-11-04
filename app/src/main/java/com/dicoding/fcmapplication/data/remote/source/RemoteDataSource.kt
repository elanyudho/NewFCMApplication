package com.dicoding.fcmapplication.data.remote.source

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.response.FdtListResponse
import com.dicoding.fcmapplication.data.remote.response.LoginResponse
import com.dicoding.fcmapplication.data.remote.service.ApiService
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(private val api: ApiService) : RemoteSafeRequest() {

    suspend fun login(loginData: Map<String, RequestBody>): Either<Failure, LoginResponse> =
        request {
            api.login(loginData)
        }

    suspend fun fdtList(): Either<Failure, List<FdtListResponse.FdtListResponseItem>> =
        request {
            api.getFdtList()
        }
}