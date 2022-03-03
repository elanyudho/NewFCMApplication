package com.dicoding.fcmapplication.data.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.CompanyProfileMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.CompanyProfile
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import timber.log.Timber
import javax.inject.Inject

class OtherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val companyProfileMapper: CompanyProfileMapper
) : OtherRepository {

    override suspend fun companyProfile(): Either<Failure, CompanyProfile> {
        return when(val response = remoteDataSource.companyProfile()) {
            is Either.Success -> {
                val companyProfile = companyProfileMapper.mapToDomain(response.body)
                Either.Success(companyProfile)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }
}