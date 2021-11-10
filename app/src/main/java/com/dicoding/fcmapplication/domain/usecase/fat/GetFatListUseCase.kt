package com.dicoding.fcmapplication.domain.usecase.fat

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.domain.repository.FatRepository
import javax.inject.Inject

class GetFatListUseCase @Inject constructor(private val repo: FatRepository) :
    UseCase<List<Fat>, Long>() {

    override suspend fun run(params: Long): Either<Failure, List<Fat>> {
        return repo.fatList(params.toString())
    }

}