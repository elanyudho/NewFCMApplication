package com.dicoding.fcmapplication.domain.model

data class Fdt(
    val fdtId: String?,
    val fdtName: String?,
    val fdtIsService: Boolean?,
    val fdtRegion: String?,
    val fdtActivated: String?,
    val fdtCore: String?,
    val fdtCoreUsed: String?,
    val fdtCoreRemaining: String?,
    val fdtNote: String?
)