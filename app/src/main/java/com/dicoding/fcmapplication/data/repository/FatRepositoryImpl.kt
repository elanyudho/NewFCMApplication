package com.dicoding.fcmapplication.data.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.FatDetailMapper
import com.dicoding.fcmapplication.data.remote.mapper.FatListMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.domain.repository.FatRepository
import timber.log.Timber
import javax.inject.Inject

class FatRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val fatListMapper: FatListMapper,
    private val fatDetailMapper: FatDetailMapper
) : FatRepository {
    override suspend fun fatList(zone: String, page: String): Either<Failure, List<Fat>> {
        return when(val response = remoteDataSource.fatList(zone, page)){
            is Either.Success -> {
                val fatList = fatListMapper.mapToDomain(response.body)
                Either.Success(fatList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun fatDetail(id: String): Either<Failure, FatDetail> {
        return when(val response = remoteDataSource.fatDetail(id)){
            is Either.Success -> {
                val fatDetail = fatDetailMapper.mapToDomain(response.body)
                Either.Success(fatDetail)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun fatSearchResult(zone: String, fatName: String, page: String): Either<Failure, List<Fat>> {
        return when(val response = remoteDataSource.fatSearchResult(zone, fatName, page)){
            is Either.Success -> {
                val fatList = fatListMapper.mapToDomain(response.body)
                Either.Success(fatList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun fatListNoPage(queries: Map<String, String>): Either<Failure, List<Fat>> {
        return when(val response = remoteDataSource.fatListNoPage(queries)){
            is Either.Success -> {
                val fatList = fatListMapper.mapToDomain(response.body)
                Either.Success(fatList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun deleteFatData(id: String): Either<Failure, Nothing?> {
        return when(val response = remoteDataSource.deleteFatData(id)) {
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