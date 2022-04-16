package com.dicoding.fcmapplication.domain.usecase.fdt

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import javax.inject.Inject

class DeleteFdtDataUseCase @Inject constructor(private val repo: FdtRepository) : UseCase<Nothing?, String>() {

    override suspend fun run(params: String): Either<Failure, Nothing?> {
        return repo.deleteFdtData(params)
    }
}