package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtListResponse
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.utils.api.ApiVariabel.BASE_URL_IMAGE

class FdtListMapper : BaseMapper<List<FdtListResponse.FdtListResponseItem>, List<Fdt>> {
    override fun mapToDomain(raw: List<FdtListResponse.FdtListResponseItem>): List<Fdt> {
        val listData = mutableListOf<Fdt>()
        for (i in raw) {
            val data = Fdt(fdtName = i.fdtName, uuid = i.uuid, fdtImage = BASE_URL_IMAGE+i.fdtImage[0].url)
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fdt>): List<FdtListResponse.FdtListResponseItem> {
        TODO("Not yet implemented")
    }


}