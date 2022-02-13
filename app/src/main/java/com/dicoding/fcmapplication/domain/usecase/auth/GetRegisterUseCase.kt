package com.dicoding.fcmapplication.domain.usecase.auth

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.User
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import com.dicoding.fcmapplication.utils.extensions.toMultipartForm
import okhttp3.RequestBody
import javax.inject.Inject

class GetRegisterUseCase @Inject constructor(private val repo : AuthRepository) :
    UseCase<User, GetRegisterUseCase.Params>(){

    data class Params(
        val email: String,
        val username: String,
        val password: String,
        val isAdmin: String,
        val isConfirmed: String,
        val isBlocked: String,
        val region: String
    )

    override suspend fun run(params: Params): Either<Failure, User> {
        val register = HashMap<String, RequestBody>()

        register["email"]=params.email.toMultipartForm()
        register["username"]=params.username.toMultipartForm()
        register["password"]=params.password.toMultipartForm()
        register["isAdmin"]=params.isAdmin.toMultipartForm()
        register["confirmed"]=params.isConfirmed.toMultipartForm()
        register["blocked"]=params.isBlocked.toMultipartForm()
        register["region"]=params.region.toMultipartForm()
        return repo.register(register)
    }
}