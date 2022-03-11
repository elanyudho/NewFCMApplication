package com.dicoding.fcmapplication.di

import com.dicoding.core.dispatcher.DispatcherProvider
import com.dicoding.fcmapplication.data.dispatcher.DispatcherProviderImpl
import com.dicoding.fcmapplication.data.remote.mapper.*
import com.dicoding.fcmapplication.data.remote.service.ApiService
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.data.repository.AuthRepositoryImpl
import com.dicoding.fcmapplication.data.repository.FatRepositoryImpl
import com.dicoding.fcmapplication.data.repository.FdtRepositoryImpl
import com.dicoding.fcmapplication.data.repository.OtherRepositoryImpl
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import com.dicoding.fcmapplication.domain.repository.FatRepository
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import com.dicoding.fcmapplication.domain.usecase.auth.GetLoginUseCase
import com.dicoding.fcmapplication.domain.usecase.auth.GetRegionListUseCase
import com.dicoding.fcmapplication.domain.usecase.auth.GetRegisterUseCase
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatDetailUseCase
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatListNoPageUseCase
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatListUseCase
import com.dicoding.fcmapplication.domain.usecase.fat.GetFatSearchResultUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtDetailUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtListNoPageUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtListUseCase
import com.dicoding.fcmapplication.domain.usecase.fdt.GetFdtSearchResultUseCase
import com.dicoding.fcmapplication.domain.usecase.other.GetCompanyProfileUseCase
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

    @Provides
    @ActivityScoped
    fun provideFdtDetailMapper() = FdtDetailMapper()

    @Provides
    @ActivityScoped
    fun provideFatDetailMapper() = FatDetailMapper()

    @Provides
    @ActivityScoped
    fun provideCompanyProfileMapper() = CompanyProfileMapper()

    @Provides
    @ActivityScoped
    fun provideRegionListMapper() = RegionListMapper()


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

    @Binds
    @ActivityScoped
    abstract fun bindOtherRepository(repositoryImpl: OtherRepositoryImpl): OtherRepository

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
    fun provideRegisterUseCase(repository: AuthRepository) = GetRegisterUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFdtListUseCase(repository: FdtRepository) = GetFdtListUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFatListUseCase(repository: FatRepository) = GetFatListUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFdtDetailUseCase(repository: FdtRepository) = GetFdtDetailUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFatDetailUseCase(repository: FatRepository) = GetFatDetailUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideCompanyProfileUseCase(repository: OtherRepository) = GetCompanyProfileUseCase(repository)


    @Provides
    @ActivityScoped
    fun provideFdtSearchResultUseCase(repository: FdtRepository) = GetFdtSearchResultUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFatSearchResultUseCase(repository: FatRepository) = GetFatSearchResultUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideRegionUseCase(repository: AuthRepository) = GetRegionListUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFdtListNoPageUseCase(repository: FdtRepository) = GetFdtListNoPageUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideFatListNoPageUseCase(repository: FatRepository) = GetFatListNoPageUseCase(repository)
}