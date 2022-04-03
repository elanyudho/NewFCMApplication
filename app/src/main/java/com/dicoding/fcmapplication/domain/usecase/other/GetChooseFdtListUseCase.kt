package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.FdtToAdd
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class GetChooseFdtListUseCase @Inject constructor(private val repository: OtherRepository) :
    UseCase<List<FdtToAdd>, String?>() {

    override suspend fun run(params: String?): Either<Failure, List<FdtToAdd>> {
        val queries = HashMap<String, String>()

        queries["_fdt_region_contains"] = params?: ""

        return repository.chooseFdt(queries)
    }
}