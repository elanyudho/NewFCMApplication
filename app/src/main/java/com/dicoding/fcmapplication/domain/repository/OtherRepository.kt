package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.*
import kotlinx.coroutines.flow.Flow

interface OtherRepository {

    suspend fun companyProfile(): Either<Failure, CompanyProfile>

    suspend fun postFdtData(postFDT: PostFDT): Either<Failure, Nothing?>

    suspend fun postFatData(postFAT: PostFAT): Either<Failure, Nothing?>

    suspend fun chooseFdt(queries: Map<String, String>): Either<Failure, List<FdtToAdd>>
}


