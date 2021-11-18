package com.dicoding.fcmapplication.domain.usecase.fdt

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import javax.inject.Inject

class GetFdtDetailUseCase @Inject constructor(
    private val repo: FdtRepository
) : UseCase<FdtDetail, String>() {
    override suspend fun run(params: String): Either<Failure, FdtDetail> {
        return repo.fdtDetail(params)
    }

}