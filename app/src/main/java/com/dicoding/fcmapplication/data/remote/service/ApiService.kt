package com.dicoding.fcmapplication.data.remote.service

import com.dicoding.fcmapplication.data.remote.response.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService{

    @JvmSuppressWildcards
    @Multipart
    @POST("auth/local")
    suspend fun login(@PartMap loginData: Map<String, RequestBody> ): Response<LoginResponse>

    @GET("fdts/page/{page}")
    suspend fun getFdtList(@Path("page") page: String): Response<List<FdtListResponse.FdtListResponseItem>>

    @GET("fats/page/{page}")
    suspend fun getFatList(@Path("page") page: String): Response<List<FatListResponse.FatListResponseItem>>

    @GET("fdtdetails/{uuid}")
    suspend fun getFdtDetail(@Path("uuid") uuid: String): Response<FdtDetailResponse>

    @GET("fatdetails/{uuid}")
    suspend fun getFatDetail(@Path("uuid") uuid: String): Response<FatDetailResponse>

    @GET("companyprofile")
    suspend fun getCompanyProfile(): Response<CompanyProfileResponse>

    @GET("servicelists/page/{page}")
    suspend fun getRepairList(@Path("page") page: String): Response<List<RepairListResponse.RepairListResponseItem>>
}