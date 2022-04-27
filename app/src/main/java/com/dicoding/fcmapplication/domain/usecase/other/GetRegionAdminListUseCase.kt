package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.RegionAdmin
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class GetRegionAdminListUseCase @Inject constructor(private val repository: OtherRepository) :
    UseCase<List<RegionAdmin>, Long>() {

    override suspend fun run(params: Long): Either<Failure, List<RegionAdmin>> {
        return repository.regionAdminList(params.toString())
    }
}