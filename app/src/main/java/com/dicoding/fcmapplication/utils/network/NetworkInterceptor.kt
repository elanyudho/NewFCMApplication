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

        return if (!session.isLogin) {
            val newRequest =
                chain.request().newBuilder()
                    .addHeader("Authorization", encryptedPreferences.encryptedToken)
                    .build()
            chain.proceed(newRequest)
        }else {
            val newRequest =
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${encryptedPreferences.encryptedToken}")
                    .build()
            chain.proceed(newRequest)
        }
    }
}