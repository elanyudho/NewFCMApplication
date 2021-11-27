package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatListResponse
import com.dicoding.fcmapplication.domain.model.Fat

class FatListMapper : BaseMapper<List<FatListResponse.FatListResponseItem>, List<Fat>> {
    override fun mapToDomain(raw: List<FatListResponse.FatListResponseItem>): List<Fat> {
        val listData = ArrayList<Fat>()
        raw.map {
            val data = Fat(
                fatName = it.fatName,
                uuid = it.uuid,
                fatImage = it.fatImage,
                fatActivateDate = it.fatActivateDate,
                fatIsService = it.fatIsService
            )
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fat>): List<FatListResponse.FatListResponseItem> {
        TODO("Not yet implemented")
    }


}