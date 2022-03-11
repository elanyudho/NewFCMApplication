package com.dicoding.fcmapplication.domain.usecase.fdt

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.repository.FdtRepository
import javax.inject.Inject

class GetFdtListNoPageUseCase @Inject constructor(private val repository: FdtRepository) :
    UseCase<List<Fdt>, String?>() {

    override suspend fun run(params: String?): Either<Failure, List<Fdt>> {
        val queries = HashMap<String, String>()

        queries["_fdt_region_contains"] = params?: ""

        return repository.fdtListNoPage(queries)
    }
}