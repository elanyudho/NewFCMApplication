package com.dicoding.fcmapplication.data.remote.mapper

import com.dicoding.core.abstraction.BaseMapper
import com.dicoding.fcmapplication.data.remote.response.FdtResponse
import com.dicoding.fcmapplication.domain.model.FdtToAdd

class ChooseFdtMapper : BaseMapper<List<FdtResponse.FdtResponseItem>, List<FdtToAdd>> {
    override fun mapToDomain(raw: List<FdtResponse.FdtResponseItem>): List<FdtToAdd> {
        return raw.map {
            FdtToAdd(
                fdtId = it.id.toString(),
                fdtName = it.fdtName
            )
        }
    }

    override fun mapToRaw(domain: List<FdtToAdd>): List<FdtResponse.FdtResponseItem> {
        TODO("Not yet implemented")
    }


}