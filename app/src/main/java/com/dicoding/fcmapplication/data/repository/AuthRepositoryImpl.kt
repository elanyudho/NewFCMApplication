package com.dicoding.fcmapplication.data.repository

import android.util.Log
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.data.remote.mapper.LoginMapper
import com.dicoding.fcmapplication.data.remote.source.RemoteDataSource
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val loginMapper : LoginMapper,
) : AuthRepository {

    override suspend fun login(loginData: HashMap<String, RequestBody>): Either<Failure, User> {
       return when(val results = remoteDataSource.login(loginData)) {

           is Either.Success -> {
               val login = loginMapper.mapToDomain(results.body)
               Either.Success(login)
           }
           is Either.Error -> {
               Log.d("failed do login :", results.failure.throwable.message.toString())
               Either.Error(results.failure)
           }
       }
    }
}