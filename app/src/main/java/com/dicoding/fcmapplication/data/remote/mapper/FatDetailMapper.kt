package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatDetailResponse
import com.dicoding.fcmapplication.data.remote.response.FatResponse
import com.dicoding.fcmapplication.domain.model.FatDetail

class FatDetailMapper : BaseMapper<FatDetailResponse, FatDetail>
{
    override fun mapToDomain(raw: FatDetailResponse): FatDetail {
        return FatDetail(
            fatName = raw.fatName,
            fatCore = raw.fatTotalCore,
            fatCoreUsed = raw.fatCoreUsed,
            fatCoreRemaining = raw.fatBackupCore,
            fatCoveredHome = raw.fatHomeCovered,
            fatIsService = raw.fatInRepair,
            fatLocation = raw.fatLocation,
            fatLoss = raw.fatLoss,
            fatNote = raw.fatNote,
            fatId = raw.id.toString(),
            fatRegion = raw.fatRegion.toString()
        )
    }

    override fun mapToRaw(domain: FatDetail):FatDetailResponse {
        return FatDetailResponse()
    }
}