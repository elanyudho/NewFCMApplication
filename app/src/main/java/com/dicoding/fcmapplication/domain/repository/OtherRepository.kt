package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.*

interface OtherRepository {

    suspend fun companyProfile(): Either<Failure, CompanyProfile>

    suspend fun postFdtData(postFDT: PostFDT): Either<Failure, Nothing?>

    suspend fun postFatData(postFAT: PostFAT): Either<Failure, Nothing?>

    suspend fun chooseFdt(queries: Map<String, String>): Either<Failure, List<FdtToAdd>>

    suspend fun coveredFat(queries: Map<String, String>): Either<Failure, List<FdtDetail.FatList>>

    suspend fun putFdtData(id: String, putFDT: PostFDT): Either<Failure, Nothing?>

    suspend fun putFatData(id: String, putFAT: PostFAT): Either<Failure, Nothing?>
}


