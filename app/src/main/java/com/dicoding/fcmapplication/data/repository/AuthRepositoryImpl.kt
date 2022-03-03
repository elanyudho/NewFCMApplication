package com.dicoding.fcmapplication.data.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.LoginMapper
import com.dicoding.fcmapplication.data.remote.mapper.RegionListMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val loginMapper: LoginMapper,
    private val regionListMapper: RegionListMapper,
) : AuthRepository {

    override suspend fun login(loginData: HashMap<String, RequestBody>): Either<Failure, User> {
        return when (val results = remoteDataSource.login(loginData)) {

            is Either.Success -> {
                val login = loginMapper.mapToDomain(results.body)
                Either.Success(login)
            }
            is Either.Error -> {
                Timber.e(results.failure.throwable.message.toString())
                Either.Error(results.failure)
            }
        }
    }

    override suspend fun register(registerData: HashMap<String, RequestBody>): Either<Failure, User> {
        return when (val results = remoteDataSource.register(registerData)) {
            is Either.Success -> {
                val register = loginMapper.mapToDomain(results.body)
                Either.Success(register)
            }
            is Either.Error -> {
                Timber.e(results.failure.throwable.message.toString())
                Either.Error(results.failure)
            }
        }
    }

    override suspend fun region(): Either<Failure, List<Region>> {
        return when (val results = remoteDataSource.regionList()) {
            is Either.Success -> {
                val register = regionListMapper.mapToDomain(results.body)
                Either.Success(register)
            }
            is Either.Error -> {
                Timber.e(results.failure.throwable.message.toString())
                Either.Error(results.failure)
            }
        }
    }
}