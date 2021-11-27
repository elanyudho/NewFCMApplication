package com.dicoding.fcmapplication.domain.usecase.fat

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.repository.FatRepository
import javax.inject.Inject

class GetFatSearchResultUseCase @Inject constructor(private val repository: FatRepository) :
    UseCase<List<Fat>, String?>() {

    override suspend fun run(params: String?): Either<Failure, List<Fat>> {
        val queries = HashMap<String, String>()

        queries["_fat_name_contains"] = params ?: ""

        return repository.fatSearchResult(queries)
    }


}