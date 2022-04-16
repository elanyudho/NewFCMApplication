package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.model.FdtDetail

interface FdtRepository {

    suspend fun fdtList(zone: String, page: String): Either<Failure, List<Fdt>>

    suspend fun fdtDetail(id: String): Either<Failure, FdtDetail>

    suspend fun fdtSearchResult(zone: String, fdtName: String, page: String) : Either<Failure, List<Fdt>>

    suspend fun fdtListNoPage(queries: Map<String, String>) : Either<Failure, List<Fdt>>

    suspend fun deleteFdtData(id: String): Either<Failure, Nothing?>

}