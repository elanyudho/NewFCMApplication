package com.dicoding.fcmapplication.data.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.FdtDetailMapper
import com.dicoding.fcmapplication.data.remote.mapper.FdtListMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import timber.log.Timber
import javax.inject.Inject

class FdtRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val fdtListMapper: FdtListMapper,
    private val fdtDetailMapper: FdtDetailMapper
) : FdtRepository {

    override suspend fun fdtList(zone: String, page: String): Either<Failure, List<Fdt>> {
        return when(val response = remoteDataSource.fdtList(zone, page)){
            is Either.Success -> {
                val fdtList = fdtListMapper.mapToDomain(response.body)
                Either.Success(fdtList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun fdtDetail(id: String): Either<Failure, FdtDetail> {
        return when(val response = remoteDataSource.fdtDetail(id)){
            is Either.Success -> {
                val fdtDetail = fdtDetailMapper.mapToDomain(response.body)
                Either.Success(fdtDetail)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun fdtSearchResult(zone: String, fdtName: String, page: String): Either<Failure, List<Fdt>> {
        return when(val response = remoteDataSource.fdtSearchResult(zone, fdtName, page)){
            is Either.Success -> {
                val fdtList = fdtListMapper.mapToDomain(response.body)
                Either.Success(fdtList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun fdtListNoPage(queries: Map<String, String>): Either<Failure, List<Fdt>> {
        return when(val response = remoteDataSource.fdtListNoPage(queries)){
            is Either.Success -> {
                val fdtList = fdtListMapper.mapToDomain(response.body)
                Either.Success(fdtList)
            }
            is Either.Error -> {
                Timber.e(response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun deleteFdtData(id: String): Either<Failure, Nothing?> {
        return when(val response = remoteDataSource.deleteFdtData(id)) {
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

