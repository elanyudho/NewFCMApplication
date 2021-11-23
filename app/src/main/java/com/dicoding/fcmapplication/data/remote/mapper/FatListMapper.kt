package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatListResponse
import com.dicoding.fcmapplication.domain.model.Fat

class FatListMapper : BaseMapper<List<FatListResponse.FatListResponseItem>, List<Fat>> {
    override fun mapToDomain(raw: List<FatListResponse.FatListResponseItem>): List<Fat> {
        val listData = mutableListOf<Fat>()
        for (i in raw) {
            val data = Fat(fatName = i.fatName, uuid = i.uuid, fatImage = i.fatImage, fatActivateDate = i.fatActivateDate, fatIsService = i.fatIsService)
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fat>): List<FatListResponse.FatListResponseItem> {
        TODO("Not yet implemented")
    }


}