package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.PostFAT
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class PostFatDataUseCase @Inject constructor(private val repository: OtherRepository) : UseCase<Nothing?, PostFAT>() {

    override suspend fun run(params: PostFAT): Either<Failure, Nothing?> {
        return repository.postFatData(params)
    }
}