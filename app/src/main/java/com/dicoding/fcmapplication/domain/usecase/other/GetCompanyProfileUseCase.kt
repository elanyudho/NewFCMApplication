package com.dicoding.fcmapplication.domain.usecase.other

import com.dicoding.core.abstraction.UseCase
import com.dicoding.core.exception.Failure
import com.dicoding.core.vo.Either
import com.dicoding.fcmapplication.domain.model.CompanyProfile
import com.dicoding.fcmapplication.domain.repository.OtherRepository
import javax.inject.Inject

class GetCompanyProfileUseCase @Inject constructor(
    private val repository: OtherRepository
) : UseCase<CompanyProfile, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, CompanyProfile> {
        return repository.companyProfile()
    }
}