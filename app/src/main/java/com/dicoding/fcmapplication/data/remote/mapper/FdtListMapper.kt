package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtListResponse
import com.dicoding.fcmapplication.domain.model.Fdt

class FdtListMapper : BaseMapper<List<FdtListResponse.FdtListResponseItem>, List<Fdt>> {
    override fun mapToDomain(raw: List<FdtListResponse.FdtListResponseItem>): List<Fdt> {
        val listData = mutableListOf<Fdt>()
        for (i in raw) {
            val data = Fdt(fdtName = i.fdtName, uuid = i.uuid, fdtImage = i.fdtImage, fdtActivateDate = i.fdtActivateDate, fdtIsService = i.fdtIsService)
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fdt>): List<FdtListResponse.FdtListResponseItem> {
        TODO("Not yet implemented")
    }


}