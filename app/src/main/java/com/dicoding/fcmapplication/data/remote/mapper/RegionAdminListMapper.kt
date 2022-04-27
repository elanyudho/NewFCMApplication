package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.BuildConfig.BASE_URL
import com.dicoding.fcmapplication.data.remote.response.RegionAdminResponse
import com.dicoding.fcmapplication.domain.model.RegionAdmin

class RegionAdminListMapper : BaseMapper<List<RegionAdminResponse.RegionAdminResponseItem>, List<RegionAdmin>> {

    override fun mapToDomain(raw: List<RegionAdminResponse.RegionAdminResponseItem>): List<RegionAdmin> {
        return raw.map {
            RegionAdmin(
                adminName = it.adminName,
                adminImage = BASE_URL + it.adminImage[0].url,
                adminPosition = it.adminPositon,
                adminEmail = it.adminEmail,
                adminRegion = it.adminRegion,
                adminPhone = it.adminPhone
            )
        }
    }

    override fun mapToRaw(domain: List<RegionAdmin>): List<RegionAdminResponse.RegionAdminResponseItem> {
        TODO("Not yet implemented")
    }
}