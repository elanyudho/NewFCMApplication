package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.domain.model.Fdt

interface FatRepository {

    suspend fun fatList(zone: String, page: String): Either<Failure, List<Fat>>

    suspend fun fatDetail(id: String): Either<Failure, FatDetail>

    suspend fun fatSearchResult(zone: String, fatName: String, page: String) : Either<Failure, List<Fat>>

    suspend fun fatListNoPage(queries: Map<String, String>) : Either<Failure, List<Fat>>

    suspend fun deleteFatData(id: String): Either<Failure, Nothing?>
}