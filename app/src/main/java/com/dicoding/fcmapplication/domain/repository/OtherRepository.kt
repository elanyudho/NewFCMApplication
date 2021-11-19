package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.CompanyProfile

interface OtherRepository {

    suspend fun companyProfile(): Either<Failure, CompanyProfile>

}