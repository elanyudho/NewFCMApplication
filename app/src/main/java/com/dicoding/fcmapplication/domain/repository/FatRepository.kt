package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.model.FatDetail

interface FatRepository {

    suspend fun fatList(page: String): Either<Failure, List<Fat>>

    suspend fun fatDetail(uuid: String): Either<Failure, FatDetail>

    suspend fun fatSearchResult(queries: Map<String, String>) : Either<Failure, List<Fat>>
}