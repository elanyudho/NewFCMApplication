package com.dicoding.fcmapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FdtDetail(
    val fdtId: String?,
    val fdtName: String?,
    val fdtCore: String?,
    val fdtCoreUsed: String?,
    val fdtCoreRemaining: String?,
    val fdtCoveredFat: String?,
    val fdtLoss: String?,
    val fdtIsService: Boolean?,
    val fdtNote: String?,
    val fdtLocation: String?,
    val fatCoveredList: List<FatList> = mutableListOf()
) : Parcelable {

    @Parcelize
    data class FatList(
        val fatId: String?,
        val fatName: String?,
        val fatActivated: String?,
        val fatIsService: Boolean?
    ): Parcelable
}