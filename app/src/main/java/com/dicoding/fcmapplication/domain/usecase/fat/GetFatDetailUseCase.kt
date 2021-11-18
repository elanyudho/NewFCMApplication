package com.dicoding.fcmapplication.domain.usecase.fat

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.domain.repository.FatRepository
import javax.inject.Inject

class GetFatDetailUseCase @Inject constructor(private val repo: FatRepository) : UseCase<FatDetail, String>() {

    override suspend fun run(params: String): Either<Failure, FatDetail> {
        return repo.fatDetail(params)
    }
}