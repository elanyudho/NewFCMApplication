package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.RepairListResponse
import com.dicoding.fcmapplication.domain.model.Repair

class RepairListMapper : BaseMapper<List<RepairListResponse.RepairListResponseItem>, List<Repair>> {
    override fun mapToDomain(raw: List<RepairListResponse.RepairListResponseItem>): List<Repair> {
        val listData = mutableListOf<Repair>()
        for (i in raw) {
            val data = Repair(deviceName = i.deviceName, uuid = i.uuid, deviceImage = i.deviceImage, deviceNote = i.deviceNote)
            listData.add(data)
        }
        return listData
    }

    override fun mapToRaw(domain: List<Repair>): List<RepairListResponse.RepairListResponseItem> {
        TODO("Not yet implemented")
    }


}