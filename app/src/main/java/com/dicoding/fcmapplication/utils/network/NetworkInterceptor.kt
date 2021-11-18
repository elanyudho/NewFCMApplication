package com.dicoding.fcmapplication.utils.network

import com.dicoding.fcmapplication.data.pref.EncryptedPreferences
import com.dicoding.fcmapplication.data.pref.Session
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(
    private val encryptedPreferences: EncryptedPreferences,
    private val session: Session
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            chain.request().newBuilder().apply {
                if(encryptedPreferences.encryptedToken.isNotEmpty())
                addHeader("Authorization", "Bearer ${encryptedPreferences.encryptedToken}")
            }.build()

        return chain.proceed(newRequest)
    }
}