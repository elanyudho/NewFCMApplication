package com.dicoding.fcmapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FatDetail(
    val fatName: String?,
    val fatCore: String?,
    val fatCoreUsed: String?,
    val fatCoreRemaining: String?,
    val fatCoveredHome: String?,
    val fatLoss: String?,
    var fatIsService: Boolean?,
    val fatNote: String?,
    val fatLocation: String?,
    val fatActivationDate: String?,
    val fatId: String?,
    val fatRegion: String?,
    val fdtBind: Fdt?
) : Parcelable {
    @Parcelize
    data class Fdt(
        val fdtId: String?,
        val fdtName: String?
    ) : Parcelable
}