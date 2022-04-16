package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.PostFAT
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class PutFatDataUseCase @Inject constructor(private val repository: OtherRepository) : UseCase<Nothing?, PutFatDataUseCase.PutFatData>() {

    data class PutFatData(
        var id: String,
        var data: PostFAT
    )

    override suspend fun run(params: PutFatData): Either<Failure, Nothing?> {
        return repository.putFatData(params.id, params.data)
    }
}