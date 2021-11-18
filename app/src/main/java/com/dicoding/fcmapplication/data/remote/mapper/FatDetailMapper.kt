package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatDetailResponse
import com.dicoding.fcmapplication.domain.model.FatDetail
import com.dicoding.fcmapplication.utils.api.ApiVariabel

class FatDetailMapper : BaseMapper<FatDetailResponse, FatDetail>
{
    override fun mapToDomain(raw: FatDetailResponse): FatDetail {
        return FatDetail(
            fatName = raw.fatName,
            fatCore = raw.fatCore,
            fatCoreUsed = raw.fatCoreUsed,
            fatCoreRemaining = raw.fatCoreRemaining,
            fatCoveredHome = raw.fatCoveredHome,
            fatIsService = raw.fatIsService,
            fatLocation = raw.fatLocation,
            fatLoss = raw.fatLoss,
            fatNote = raw.fatNote,
            fatImage = raw.fatImage,
            uuid = raw.fatUuid
        )
    }

    override fun mapToRaw(domain: FatDetail): FatDetailResponse {
        return FatDetailResponse()
    }
}