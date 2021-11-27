package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtListResponse
import com.dicoding.fcmapplication.domain.model.Fdt

class FdtListMapper : BaseMapper<List<FdtListResponse.FdtListResponseItem>, List<Fdt>> {
    override fun mapToDomain(raw: List<FdtListResponse.FdtListResponseItem>): List<Fdt> {
        val listData = ArrayList<Fdt>()
        raw.map {
            val data = Fdt(
                fdtName = it.fdtName,
                uuid = it.uuid,
                fdtImage = it.fdtImage,
                fdtActivateDate = it.fdtActivateDate,
                fdtIsService = it.fdtIsService)
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fdt>): List<FdtListResponse.FdtListResponseItem> {
        TODO("Not yet implemented")
    }


}