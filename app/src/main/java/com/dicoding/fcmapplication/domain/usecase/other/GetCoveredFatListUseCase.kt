package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class GetCoveredFatListUseCase @Inject constructor(private val repository: OtherRepository) :
    UseCase<List<FdtDetail.FatList>, String?>() {

    override suspend fun run(params: String?): Either<Failure, List<FdtDetail.FatList>> {
        val queries = HashMap<String, String>()

        queries["_fat_name_contains"] = params?: ""

        return repository.coveredFat(queries)
    }
}