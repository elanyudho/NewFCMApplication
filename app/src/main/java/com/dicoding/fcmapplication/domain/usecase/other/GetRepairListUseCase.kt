package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Repair
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class GetRepairListUseCase @Inject constructor(private val repo: OtherRepository) :
    UseCase<List<Repair>, Long>(){

    override suspend fun run(params: Long): Either<Failure, List<Repair>> {
        return repo.repairList(params.toString())
    }
}