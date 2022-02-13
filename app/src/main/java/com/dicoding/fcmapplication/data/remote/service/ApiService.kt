package com.dicoding.fcmapplication.data.remote.service

import com.dicoding.fcmapplication.data.remote.response.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @JvmSuppressWildcards
    @Multipart
    @POST("auth/local")
    suspend fun login(@PartMap loginData: Map<String, RequestBody>): Response<LoginResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("auth/local/register")
    suspend fun register(@PartMap registerData: Map<String, RequestBody>): Response<LoginResponse>

    @GET("fdts/zone/{zone}/fdtname/%00/page/{page}")
    suspend fun getFdtList(
        @Path("zone") zone: String,
        @Path("page") page: String
    ): Response<List<FdtResponse.FdtResponseItem>>

    @GET("fats/zone/{zone}/fatname/%00/page/{page}")
    suspend fun getFatList(
        @Path("zone") zone: String,
        @Path("page") page: String
    ): Response<List<FatResponse.FatResponseItem>>

    @GET("fdts/{fdt_name}")
    suspend fun getFdtDetail(@Path("fdt_name") uuid: String): Response<FdtDetailResponse>

    @GET("fats/{fat_name}")
    suspend fun getFatDetail(@Path("fat_name") uuid: String): Response<FatDetailResponse>

    @GET("companyprofile")
    suspend fun getCompanyProfile(): Response<CompanyProfileResponse>

    @GET("servicelists/page/{page}")
    suspend fun getRepairList(@Path("page") page: String): Response<List<RepairListResponse.RepairListResponseItem>>

    @GET("fdts/zone/{zone}/fdtname/{fdtname}/page/{page}")
    suspend fun getFdtSearchResult(
        @Path("zone") zone: String,
        @Path("fdtname") fdtname: String,
        @Path("page") page: String
    ): Response<List<FdtResponse.FdtResponseItem>>

    @GET("fdts/zone/{zone}/fatname/{fatname}/page/{page}")
    suspend fun getFatSearchResult(
        @Path("zone") zone: String,
        @Path("fatname") fatname: String,
        @Path("page") page: String
    ): Response<List<FatResponse.FatResponseItem>>
}