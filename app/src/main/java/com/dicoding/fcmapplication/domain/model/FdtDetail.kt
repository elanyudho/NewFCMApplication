package com.dicoding.fcmapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FdtDetail(
    val fdtName: String?,
    val fdtCore: String?,
    val fdtCoreUsed: String?,
    val fdtCoreRemaining: String?,
    val fdtCoveredFat: String?,
    val fdtLoss: String?,
    val fdtIsService: Boolean?,
    val fdtNote: String?,
    val fdtLocation: String?,
    val uuid: String?,
    val fdtImage: String?,
    val fdtCoveredList: List<FatList> = mutableListOf()
) : Parcelable {
    
    @Parcelize
    data class FatList(
        val fatName: String?,
        val fatUuid: String?,
        val fatImage: String?
    ):Parcelable
}