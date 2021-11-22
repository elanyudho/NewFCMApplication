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
                if (session.isLogin){
                    addHeader("Authorization", "Bearer ${encryptedPreferences.encryptedToken}")
                }
                if (!session.isLogin) {
                    if (!session.isFirstTime) {
                        removeHeader("Authorization")
                    }
                }
            }.build()

        return chain.proceed(newRequest)
    }
}