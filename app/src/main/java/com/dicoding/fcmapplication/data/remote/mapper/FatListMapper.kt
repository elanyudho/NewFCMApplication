package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatResponse
import com.dicoding.fcmapplication.domain.model.Fat

class FatListMapper : BaseMapper<List<FatResponse.FatResponseItem>, List<Fat>> {
    override fun mapToDomain(raw: List<FatResponse.FatResponseItem>): List<Fat> {
        val listData = ArrayList<Fat>()
        raw.map {
            val data = Fat(
                fatName = it.fatName,
                fatId = it.id.toString(),
                fatActivated = it.fatActivated,
                fatIsService = it.fatInRepair,
                fatRegion = it.fatRegion,
                fatCore = it.fatTotalCore,
                fatCoreUsed = it.fatCoreUsed,
                fatCoreRemaining = it.fatBackupCore
            )
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fat>): List<FatResponse.FatResponseItem> {
        TODO("Not yet implemented")
    }


}