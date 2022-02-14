package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.domain.model.User
import okhttp3.RequestBody

interface AuthRepository {

    suspend fun login(loginData :HashMap<String, RequestBody>): Either<Failure, User>

    suspend fun register(registerData :HashMap<String, RequestBody>): Either<Failure, User>

    suspend fun region(): Either<Failure, List<Region>>
}