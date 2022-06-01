package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatResponse
import com.dicoding.fcmapplication.domain.model.FdtDetail

class CoveredFatMapper : BaseMapper<List<FatResponse.FatResponseItem>, List<FdtDetail.FatList>> {
    override fun mapToDomain(raw: List<FatResponse.FatResponseItem>): List<FdtDetail.FatList> {
        return raw.map {
            FdtDetail.FatList(
                fatName = it.fatName,
                fatActivated = it.fatActivated,
                fatId = it.id.toString(),
                fatIsService = it.fatInRepair,
                coreUsed = it.fatCoreUsed,
                total = it.fatTotalCore,
                fdt = if (it.fdt == null){""} else {it.fdt.fdtName}
            )
        }
    }

    override fun mapToRaw(domain: List<FdtDetail.FatList>): List<FatResponse.FatResponseItem> {
        TODO("Not yet implemented")
    }

}