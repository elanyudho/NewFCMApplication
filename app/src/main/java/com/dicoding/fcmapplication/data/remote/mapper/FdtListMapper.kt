package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtResponse
import com.dicoding.fcmapplication.domain.model.Fdt

class FdtListMapper : BaseMapper<List<FdtResponse.FdtResponseItem>, List<Fdt>> {
    override fun mapToDomain(raw: List<FdtResponse.FdtResponseItem>): List<Fdt> {
        val listData = ArrayList<Fdt>()
        raw.map {
            val data = Fdt(
                fdtName = it.fdtName,
                fdtId = it.id.toString(),
                fdtActivated = it.fdtActivated,
                fdtIsService = it.fdtInRepair,
                fdtRegion = it.fdtRegion
            )
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fdt>): List<FdtResponse.FdtResponseItem> {
        TODO("Not yet implemented")
    }


}