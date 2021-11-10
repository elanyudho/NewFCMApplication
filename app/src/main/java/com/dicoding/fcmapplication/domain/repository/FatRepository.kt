package com.dicoding.fcmapplication.domain.repository

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fat

interface FatRepository {

    suspend fun fatList(page: String): Either<Failure, List<Fat>>
}