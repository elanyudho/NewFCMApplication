package com.dicoding.fcmapplication.domain.usecase.auth

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.Region
import com.dicoding.fcmapplication.domain.repository.AuthRepository
import javax.inject.Inject

class GetRegionListUseCase @Inject constructor(val repo: AuthRepository) :
    UseCase<List<Region>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, List<Region>> {
        return repo.region()
    }
}