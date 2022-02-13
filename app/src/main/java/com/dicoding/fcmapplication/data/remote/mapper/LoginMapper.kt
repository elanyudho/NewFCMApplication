package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.LoginResponse
import com.dicoding.fcmapplication.domain.model.User

class LoginMapper : BaseMapper<LoginResponse, User> {
    override fun mapToDomain(raw: LoginResponse): User {
        return User(
            username = raw.user.username,
            token = raw.jwt,
            isAdmin = raw.user.isAdmin,
            region = raw.user.region
        )
    }

    override fun mapToRaw(domain: User): LoginResponse {
        return LoginResponse()
    }
}