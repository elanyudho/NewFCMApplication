package com.dicoding.fcmapplication.data.repository

import android.util.Log
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.FdtListMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import javax.inject.Inject

class FdtRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val fdtListMapper: FdtListMapper
) : FdtRepository {

    override suspend fun fdtList(): Either<Failure, List<Fdt>> {
        return when(val response = remoteDataSource.fdtList()){
            is Either.Success -> {
                val fdtList = fdtListMapper.mapToDomain(response.body)
                Either.Success(fdtList)
            }
            is Either.Error -> {
                Log.d("failed get fdtList :", response.failure.throwable.message.toString())
                Either.Error(response.failure)
            }
        }
    }


}

