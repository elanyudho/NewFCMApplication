package com.dicoding.fcmapplication.utils.network

import com.dicoding.fcmapplication.data.pref.EncryptedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val encryptedPreferences: EncryptedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            chain.request().newBuilder()
                .addHeader("Authorization", encryptedPreferences.encryptedToken)
                .addHeader("Accept", "application/json")
                .build()

        return chain.proceed(newRequest)
    }
}