package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.RegionResponse
import com.dicoding.fcmapplication.domain.model.Fdt
import com.dicoding.fcmapplication.domain.model.Region

class RegionListMapper : BaseMapper<List<RegionResponse.RegionResponseItem>, List<Region>> {
    override fun mapToDomain(raw: List<RegionResponse.RegionResponseItem>): List<Region> {
        val listData = ArrayList<Region>()
        raw.map {
            val data = Region(
                regionName = it.regionName
            )
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Region>): List<RegionResponse.RegionResponseItem> {
        TODO("Not yet implemented")
    }
}