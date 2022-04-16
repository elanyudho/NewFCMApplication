package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class PutFdtDataUseCase @Inject constructor(private val repository: OtherRepository) : UseCase<Nothing?, PutFdtDataUseCase.PutFdtData>() {

    data class PutFdtData(
        var id: String,
        var data: PostFDT
    )

    override suspend fun run(params: PutFdtData): Either<Failure, Nothing?> {
        return repository.putFdtData(params.id, params.data)
    }
}