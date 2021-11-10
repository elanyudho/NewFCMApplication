package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FatListResponse
import com.dicoding.fcmapplication.domain.model.Fat
import com.dicoding.fcmapplication.utils.api.ApiVariabel

class FatListMapper : BaseMapper<List<FatListResponse.FatListResponseItem>, List<Fat>> {
    override fun mapToDomain(raw: List<FatListResponse.FatListResponseItem>): List<Fat> {
        val listData = mutableListOf<Fat>()
        for (i in raw) {
            val data = Fat(fatName = i.fatName, uuid = i.uuid, fatImage = ApiVariabel.BASE_URL_IMAGE +i.fatImage[0].url)
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Fat>): List<FatListResponse.FatListResponseItem> {
        TODO("Not yet implemented")
    }


}