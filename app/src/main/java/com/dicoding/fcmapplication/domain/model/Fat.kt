package com.dicoding.fcmapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fat(
    val fatId: String?,
    val fatName: String?,
    val fatRegion: String?,
    val fatActivated: String?,
    val fatIsService: Boolean?,
    val fatCoreUsed: String?,
    val fatCoreRemaining: String?,
    val fatCore: String?,
    val fatNote: String?
) : Parcelable