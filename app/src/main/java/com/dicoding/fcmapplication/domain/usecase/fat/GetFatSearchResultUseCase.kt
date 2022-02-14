package com.dicoding.fcmapplication.domain.usecase.fat

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.repository.FatRepository
import javax.inject.Inject

class GetFatSearchResultUseCase @Inject constructor(private val repository: FatRepository) :
    UseCase<List<Fat>, GetFatSearchResultUseCase.Params>() {

    data class Params(
        val zone: String,
        val fatName: String = "%00",
        val page: Long
    )


    override suspend fun run(params: Params): Either<Failure, List<Fat>> {

        return repository.fatSearchResult(params.zone, params.fatName, params.page.toString())
    }


}