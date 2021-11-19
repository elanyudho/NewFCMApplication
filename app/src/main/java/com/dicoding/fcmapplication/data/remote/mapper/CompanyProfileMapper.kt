package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.CompanyProfileResponse
import com.dicoding.fcmapplication.domain.model.CompanyProfile

class CompanyProfileMapper : BaseMapper<CompanyProfileResponse, CompanyProfile> {
    override fun mapToDomain(raw: CompanyProfileResponse): CompanyProfile {
        return CompanyProfile(
            companyName = raw.companyName,
            companyImage = raw.companyImage,
            companyBanner = raw.companyBanner,
            companyDetail = raw.companyDetail
        )
    }

    override fun mapToRaw(domain: CompanyProfile): CompanyProfileResponse {
        return CompanyProfileResponse()
    }
}