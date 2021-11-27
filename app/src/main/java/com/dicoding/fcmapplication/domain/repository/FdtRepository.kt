package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.model.FdtDetail

interface FdtRepository {

    suspend fun fdtList(page: String): Either<Failure, List<Fdt>>

    suspend fun fdtDetail(uuid: String): Either<Failure, FdtDetail>

    suspend fun fdtSearchResult(queries: Map<String, String>) : Either<Failure, List<Fdt>>
}