package com.dicoding.fcmapplication.domain.usecase.auth

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import com.dicoding.fcmapplication.utils.extensions.toMultipartForm
import okhttp3.RequestBody
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(private val repo : AuthRepository) :
    UseCase<User, GetLoginUseCase.Params>(){

    data class Params(
        val username:String,
        val password:String,
    )

    override suspend fun run(params: Params): Either<Failure, User> {
        val login = HashMap<String, RequestBody>()

        login["identifier"]=params.username.toMultipartForm()
        login["password"]=params.password.toMultipartForm()

        return repo.login(login)
    }
}