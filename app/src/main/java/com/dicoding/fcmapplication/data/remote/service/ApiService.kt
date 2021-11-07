package com.dicoding.fcmapplication.data.remote.service

import com.dicoding.fcmapplication.data.remote.response.FdtListResponse
import com.dicoding.fcmapplication.data.remote.response.LoginResponse
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
}