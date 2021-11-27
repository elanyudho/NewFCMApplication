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

    suspend fun fdtList(page: String): Either<Failure, List<FdtListResponse.FdtListResponseItem>> =
        request {
            api.getFdtList(page)
        }

    suspend fun fatList(page: String): Either<Failure, List<FatListResponse.FatListResponseItem>> =
        request {
            api.getFatList(page)
        }

    suspend fun fdtDetail(uuid: String): Either<Failure, FdtDetailResponse> =
        request {
            api.getFdtDetail(uuid)
        }

    suspend fun fatDetail(uuid: String): Either<Failure, FatDetailResponse> =
        request {
            api.getFatDetail(uuid)
        }

    suspend fun companyProfile(): Either<Failure, CompanyProfileResponse> =
        request {
            api.getCompanyProfile()
        }

    suspend fun repairList(page: String): Either<Failure, List<RepairListResponse.RepairListResponseItem>> =
        request {
            api.getRepairList(page)
        }

    suspend fun fdtSearchResult(queries: Map<String, String>) : Either<Failure, List<FdtListResponse.FdtListResponseItem>> =
        request{
            api.getFdtSearchResult(queries)
        }

    suspend fun fatSearchResult(queries: Map<String, String>) : Either<Failure, List<FatListResponse.FatListResponseItem>> =
        request{
            api.getFatSearchResult(queries)
        }
}