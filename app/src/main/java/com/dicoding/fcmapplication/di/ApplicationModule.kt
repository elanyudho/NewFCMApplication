package com.dicoding.fcmapplication.di


import android.content.Context
import com.dicoding.fcmapplication.data.pref.EncryptedPreferences
import com.dicoding.fcmapplication.data.pref.Session
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.dicoding.fcmapplication.data.remote.service.ApiService
import com.dicoding.fcmapplication.utils.api.ApiVariabel.BASE_URL
import com.dicoding.fcmapplication.utils.network.NetworkInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // ApiService, Okhttp3, Retrofit2
    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson =
        GsonBuilder()
            .setLenient()
            .create()

    @Provides
    @Singleton
    fun provideNetworkInterceptor(encryptedPreferences: EncryptedPreferences) = NetworkInterceptor(encryptedPreferences)

    @Provides
    @Singleton
    fun provideOkHttpClient(networkInterceptor: NetworkInterceptor): OkHttpClient {
        val timeOut = 60L

        return OkHttpClient.Builder()
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(networkInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    // Shared Preference
    @Provides
    @Singleton
    fun provideSession(@ApplicationContext context: Context) = Session(context)

    @Provides
    @Singleton
    fun provideEncryptedPreferences(@ApplicationContext context: Context) = EncryptedPreferences(context)
}