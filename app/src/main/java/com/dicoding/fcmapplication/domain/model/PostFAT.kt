package com.dicoding.fcmapplication.domain.model

data class PostFAT (
    val fatName: String?,
    val fatCore: String?,
    val fatCoreUsed: String?,
    val fatCoreRemaining: String?,
    val fatLoss: String?,
    val fatIsService: Boolean?,
    val fatNote: String?,
    val fatLocation: String?,
    val fatActivatedDate: String?,
    val fatRegion: String?
)