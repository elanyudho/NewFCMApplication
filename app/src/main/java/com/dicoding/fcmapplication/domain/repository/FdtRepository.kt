package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fdt

interface FdtRepository {

    suspend fun fdtList(): Either<Failure, List<Fdt>>
}