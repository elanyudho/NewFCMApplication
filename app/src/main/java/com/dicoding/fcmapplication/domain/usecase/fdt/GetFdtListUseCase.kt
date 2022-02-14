package com.dicoding.fcmapplication.domain.usecase.fdt

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import javax.inject.Inject

class GetFdtListUseCase @Inject constructor(private val repo: FdtRepository) :
UseCase<List<Fdt>, GetFdtListUseCase.Params>(){

    data class Params(
        val zone: String,
        val page: Long
    )

    override suspend fun run(params: Params): Either<Failure, List<Fdt>> {
        return repo.fdtList(params.zone, params.page.toString())
    }
}