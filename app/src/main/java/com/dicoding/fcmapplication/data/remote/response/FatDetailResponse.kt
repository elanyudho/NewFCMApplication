package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

data class FatDetailResponse(
    @SerializedName("fat_core")
    val fatCore: String = "",
    @SerializedName("fat_core_remaining")
    val fatCoreRemaining: String = "",
    @SerializedName("fat_core_used")
    val fatCoreUsed: String = "",
    @SerializedName("fat_covered_home")
    val fatCoveredHome: String = "",
    @SerializedName("fat_image")
    val fatImage: String = "",
    @SerializedName("fat_isService")
    val fatIsService: Boolean = false,
    @SerializedName("fat_location")
    val fatLocation: String = "",
    @SerializedName("fat_loss")
    val fatLoss: String = "",
    @SerializedName("fat_name")
    val fatName: String = "",
    @SerializedName("fat_note")
    val fatNote: String = "",
    @SerializedName("fat_uuid")
    val fatUuid: String = ""
)