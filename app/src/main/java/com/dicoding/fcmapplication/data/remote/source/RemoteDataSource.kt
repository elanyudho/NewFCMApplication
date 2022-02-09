package com.dicoding.fcmapplication.data.remote.source

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.response.*
import com.dicoding.fcmapplication.data.remote.service.ApiService
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(private val api: ApiService) : RemoteSafeRequest() {

    suspend fun login(loginData: Map<String, RequestBody>): Either<Failure, LoginResponse> =
        request {
            api.login(loginData)
        }

    suspend fun register(registerData: Map<String, RequestBody>): Either<Failure, LoginResponse> =
        request {
            api.register(registerData)
        }

    suspend fun fdtList(zone: String,page: String): Either<Failure, List<FdtResponse.FdtResponseItem>> =
        request {
            api.getFdtList(zone, page)
        }

    suspend fun fatList(zone: String, page: String): Either<Failure, List<FatResponse.FatResponseItem>> =
        request {
            api.getFatList(zone, page)
        }

    suspend fun fdtDetail(fdtName: String): Either<Failure, FdtDetailResponse> =
        request {
            api.getFdtDetail(fdtName)
        }

    suspend fun fatDetail(fatName: String): Either<Failure, FatDetailResponse> =
        request {
            api.getFatDetail(fatName)
        }

    suspend fun companyProfile(): Either<Failure, CompanyProfileResponse> =
        request {
            api.getCompanyProfile()
        }

    suspend fun repairList(page: String): Either<Failure, List<RepairListResponse.RepairListResponseItem>> =
        request {
            api.getRepairList(page)
        }

    suspend fun fdtSearchResult(queries: Map<String, String>) : Either<Failure, List<FdtResponse.FdtResponseItem>> =
        request{
            api.getFdtSearchResult(queries)
        }

    suspend fun fatSearchResult(queries: Map<String, String>) : Either<Failure, List<FatResponse.FatResponseItem>> =
        request{
            api.getFatSearchResult(queries)
        }
}