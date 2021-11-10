package com.dicoding.fcmapplication.data.repository

import android.util.Log
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.FatListMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.repository.FatRepository
import javax.inject.Inject

class FatRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val fatListMapper: FatListMapper
) : FatRepository {
    override suspend fun fatList(page: String): Either<Failure, List<Fat>> {
        return when(val response = remoteDataSource.fatList(page)){
            is Either.Success -> {
                val fatList = fatListMapper.mapToDomain(response.body)
                Either.Success(fatList)
            }
            is Either.Error -> {
                Log.d("failed get fatList :", response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }
}