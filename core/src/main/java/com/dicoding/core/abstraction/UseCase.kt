package com.dicoding.core.abstraction

import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import kotlinx.coroutines.flow.Flow

abstract class UseCase<out Type, in Params> {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    object None

}