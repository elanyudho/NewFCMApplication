package com.dicoding.fcmapplication.domain.model

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
    val fatId: String?,
    val fatRegion: String?
)