package com.dicoding.fcmapplication.data.remote.service

import com.dicoding.fcmapplication.data.remote.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap

interface ApiService{

    @JvmSuppressWildcards
    @Multipart
    @POST("auth/local")
    suspend fun login(@PartMap loginData: Map<String, RequestBody> ): Response<LoginResponse>
}