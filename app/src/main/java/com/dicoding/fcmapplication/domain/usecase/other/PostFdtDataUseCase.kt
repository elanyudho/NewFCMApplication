package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.PostFDT
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class PostFdtDataUseCase @Inject constructor(private val repository: OtherRepository) : UseCase<Nothing?, PostFDT>() {

    override suspend fun run(params: PostFDT): Either<Failure, Nothing?> {
        return repository.postFdtData(params)
    }
}