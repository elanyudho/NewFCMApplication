package com.dicoding.fcmapplication.domain.usecase.fdt

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import javax.inject.Inject

class GetFdtSearchResultUseCase @Inject constructor(private val repository: FdtRepository) :
UseCase<List<Fdt>, GetFdtSearchResultUseCase.Params>() {

    data class Params(
        var zone: String,
        var fdtName: String = "%00",
        val page: Long
    )

    override suspend fun run(params: Params): Either<Failure, List<Fdt>> {

        if (params.fdtName == "" || params.fdtName.isNullOrEmpty()){
            params.fdtName = "null"
        }

        if (params.zone == "" || params.zone.isNullOrEmpty()){
            params.zone = "null"
        }

        return repository.fdtSearchResult(params.zone, params.fdtName, params.page.toString())
    }


}