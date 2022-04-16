package com.dicoding.fcmapplication.data.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.ChooseFdtMapper
import com.dicoding.fcmapplication.data.remote.mapper.CompanyProfileMapper
import com.dicoding.fcmapplication.data.remote.mapper.CoveredFatMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.*
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import timber.log.Timber
import javax.inject.Inject

class OtherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val companyProfileMapper: CompanyProfileMapper,
    private val chooseFdtMapper: ChooseFdtMapper,
    private val coveredFatMapper: CoveredFatMapper
) : OtherRepository {

    override suspend fun companyProfile(): Either<Failure, CompanyProfile> {
        return when (val response = remoteDataSource.companyProfile()) {
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

    override suspend fun postFdtData(postFDT: PostFDT): Either<Failure, Nothing?> {
        return when (val response = remoteDataSource.postFdtData(postFDT)) {
            is Either.Success -> {
                Either.Success(null)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun postFatData(postFAT: PostFAT): Either<Failure, Nothing?> {
        return when (val response = remoteDataSource.postFatData(postFAT)) {
            is Either.Success -> {
                Either.Success(null)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun chooseFdt(queries: Map<String, String>): Either<Failure, List<FdtToAdd>> {
        return when (val response = remoteDataSource.fdtListNoPage(queries)) {
            is Either.Success -> {
                val fdtList = chooseFdtMapper.mapToDomain(response.body)
                Either.Success(fdtList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun coveredFat(queries: Map<String, String>): Either<Failure, List<FdtDetail.FatList>> {
        return when (val response = remoteDataSource.fatListNoPage(queries)) {
            is Either.Success -> {
                val fatList = coveredFatMapper.mapToDomain(response.body)
                Either.Success(fatList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun putFdtData(id: String, putFDT: PostFDT): Either<Failure, Nothing?> {
        return when (val response = remoteDataSource.putFdtData(id, putFDT)) {
            is Either.Success -> {
                Either.Success(null)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun putFatData(id: String, putFAT: PostFAT): Either<Failure, Nothing?> {
        return when (val response = remoteDataSource.putFatData(id, putFAT)) {
            is Either.Success -> {
                Either.Success(null)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

}