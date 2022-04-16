package com.dicoding.fcmapplication.data.remote.source

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.response.*
import com.dicoding.fcmapplication.data.remote.service.ApiService
import com.dicoding.fcmapplication.domain.model.PostFAT
import com.dicoding.fcmapplication.domain.model.PostFDT
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

    suspend fun fdtSearchResult(zone: String, fdtName: String, page: String) : Either<Failure, List<FdtResponse.FdtResponseItem>> =
        request{
            api.getFdtSearchResult(zone, fdtName, page)
        }

    suspend fun fatSearchResult(zone: String, fatName: String, page: String) : Either<Failure, List<FatResponse.FatResponseItem>> =
        request{
            api.getFatSearchResult(zone, fatName, page)
        }

    suspend fun regionList(): Either<Failure, List<RegionResponse.RegionResponseItem>> =
        request {
            api.getRegionList()
        }

    suspend fun fdtListNoPage(queries: Map<String, String>) : Either<Failure, List<FdtResponse.FdtResponseItem>> =
        request{
            api.getFdtListNoPage(queries)
        }

    suspend fun fatListNoPage(queries: Map<String, String>) : Either<Failure, List<FatResponse.FatResponseItem>> =
        request{
            api.getFatListNoPage(queries)
        }

    suspend fun postFdtData(postFDT: PostFDT) : Either<Failure, PostFdtResponse> =
        request{
            api.postFDTData(postFDT)
        }

    suspend fun postFatData(postFAT: PostFAT) : Either<Failure, PostFatResponse> =
        request{
            api.postFATData(postFAT)
        }

    suspend fun putFdtData(id: String, putFDT: PostFDT) : Either<Failure, PostFdtResponse> =
        request{
            api.putFDTData(id, putFDT)
        }

    suspend fun putFatData(id: String, putFAT: PostFAT) : Either<Failure, PostFatResponse> =
        request{
            api.putFATData(id, putFAT)
        }

    suspend fun deleteFdtData(id: String) : Either<Failure, PostFdtResponse> =
        request {
            api.deleteFdtData(id)
        }

    suspend fun deleteFatData(id: String) : Either<Failure, PostFatResponse> =
        request {
            api.deleteFatData(id)
        }
}