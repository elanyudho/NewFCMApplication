package com.dicoding.fcmapplication.di

import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.fcmapplication.data.dispatcher.DispatcherProviderImpl
import com.dicoding.fcmapplication.data.remote.mapper.FatListMapper
import com.dicoding.fcmapplication.data.remote.mapper.FdtListMapper
import com.dicoding.fcmapplication.data.remote.mapper.LoginMapper
import com.dicoding.fcmapplication.data.remote.service.ApiService
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.data.repository.AuthRepositoryImpl
import com.dicoding.fcmapplication.data.repository.FatRepositoryImpl
import com.dicoding.fcmapplication.data.repository.FdtRepositoryImpl
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import com.dicoding.fcmapplication.domain.repository.FatRepository
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import com.dicoding.fcmapplication.domain.usecase.auth.GetLoginUseCase
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatListUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtListUseCase
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

    @Provides
    @ActivityScoped
    fun provideLoginMapper() = LoginMapper()

    @Provides
    @ActivityScoped
    fun provideFdtListMapper() = FdtListMapper()

    @Provides
    @ActivityScoped
    fun provideFatListMapper() = FatListMapper()

}

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    @ActivityScoped
    abstract fun bindAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @ActivityScoped
    abstract fun bindFdtRepository(repositoryImpl: FdtRepositoryImpl): FdtRepository

    @Binds
    @ActivityScoped
    abstract fun bindFatRepository(repositoryImpl: FatRepositoryImpl): FatRepository

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
    @Provides
    @ActivityScoped
    fun provideLoginUseCase(repository: AuthRepository) = GetLoginUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFdtListUseCase(repository: FdtRepository) = GetFdtListUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFatListUseCase(repository: FatRepository) = GetFatListUseCase(repository)


}