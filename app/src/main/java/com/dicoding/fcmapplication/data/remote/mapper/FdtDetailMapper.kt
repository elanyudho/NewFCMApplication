package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtDetailResponse
import com.dicoding.fcmapplication.domain.model.FdtDetail

class FdtDetailMapper : BaseMapper<FdtDetailResponse, FdtDetail> {
    override fun mapToDomain(raw: FdtDetailResponse): FdtDetail {
        val listFatCovered = mutableListOf<FdtDetail.FatList>()
        raw.fatCoveredLists.map {
            val data = FdtDetail.FatList(
                fatName = it.fatName,
                fatIsService = it.fatInRepair,
                fatId = it.id.toString(),
                fatActivated = it.fatActivated,
                total = it.fatTotalCore,
                coreUsed = it.fatCoreUsed,
                fdt = raw.fdtName
            )
            listFatCovered.add(data)
        }
        return FdtDetail(
            fdtName = raw.fdtName,
            fdtCore = raw.fdtTotalCore,
            fdtCoreRemaining = raw.fdtBackupCore,
            fdtCoreUsed = raw.fdtCoreUsed,
            fdtCoveredFat = listFatCovered.count().toString(),
            fdtIsService = raw.fdtInRepair,
            fdtLocation = raw.fdtLocation,
            fdtLoss = raw.fdtLoss,
            fdtNote = raw.fdtNote,
            fatCoveredList = listFatCovered,
            fdtId = raw.id.toString(),
            fdtActivationDate = raw.fdtActivated
        )
    }

    override fun mapToRaw(domain: FdtDetail): FdtDetailResponse {
        TODO("Not yet implemented")
    }
}