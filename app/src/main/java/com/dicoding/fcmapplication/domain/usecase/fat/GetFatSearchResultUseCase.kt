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
        var zone: String,
        var fatName: String = "%00",
        val page: Long
    )


    override suspend fun run(params: Params): Either<Failure, List<Fat>> {

        if (params.fatName == "" || params.fatName.isNullOrEmpty()){
            params.fatName = "null"
        }

        if (params.zone == "" || params.zone.isNullOrEmpty()){
            params.zone = "null"
        }

        return repository.fatSearchResult(params.zone, params.fatName, params.page.toString())
    }


}