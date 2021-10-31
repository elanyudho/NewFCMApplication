package com.dicoding.fcmapplication.di

import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.fcmapplication.data.dispatcher.DispatcherProviderImpl
import com.dicoding.fcmapplication.data.remote.service.ApiService
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object DataSourceModule {

    // DataSource
    @Provides
    @ActivityScoped
    fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)
}

@Module
@InstallIn(ActivityComponent::class)
object MapperModule {

}

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

}

@Module
@InstallIn(ActivityComponent::class)
abstract class CoroutinesDispatcherModule {

    // Dispatchers
    @Binds
    @ActivityScoped
    abstract fun bindDispatcherProvider(dispatcherProvider: DispatcherProviderImpl): DispatcherProvider
}

@Module
@InstallIn(ActivityComponent::class)
object UseCaseModule {

}