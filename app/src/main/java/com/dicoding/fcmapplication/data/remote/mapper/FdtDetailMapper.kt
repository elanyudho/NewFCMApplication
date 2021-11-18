package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtDetailResponse
import com.dicoding.fcmapplication.domain.model.FdtDetail
import com.dicoding.fcmapplication.utils.api.ApiVariabel

class FdtDetailMapper : BaseMapper<FdtDetailResponse, FdtDetail> {

    override fun mapToDomain(raw: FdtDetailResponse): FdtDetail {
        val listFdtCovered = mutableListOf<FdtDetail.FatList>()
        for (i in raw.coveredFat) {
            val data = FdtDetail.FatList(
                fatName = i.fatName,
                fatUuid = i.uuid,
                fatImage = i.fatImage
            )
            listFdtCovered.add(data)
        }
        return FdtDetail(
            fdtName = raw.fdtName,
            fdtCore = raw.fdtCore,
            fdtCoreRemaining = raw.fdtCoreRemaining,
            fdtCoreUsed = raw.fdtCoreUsed,
            fdtCoveredFat = raw.fdtCoveredFat,
            fdtImage = raw.fdtImage,
            fdtIsService = raw.fdtIsService,
            fdtLocation = raw.fdtLocation,
            fdtLoss = raw.fdtLoss,
            fdtNote = raw.fdtNote,
            fdtCoveredList = listFdtCovered,
            uuid = raw.fdtUuid
        )
    }

    override fun mapToRaw(domain: FdtDetail): FdtDetailResponse {
        return FdtDetailResponse()
    }
}