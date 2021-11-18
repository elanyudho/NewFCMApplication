package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

data class FdtDetailResponse(
    @SerializedName("covered_fat")
    val coveredFat: List<CoveredFat> = listOf(),
    @SerializedName("fdt_core")
    val fdtCore: String = "",
    @SerializedName("fdt_core_remaining")
    val fdtCoreRemaining: String = "",
    @SerializedName("fdt_core_used")
    val fdtCoreUsed: String = "",
    @SerializedName("fdt_covered_fat")
    val fdtCoveredFat: String = "",
    @SerializedName("fdt_image")
    val fdtImage: String = "",
    @SerializedName("fdt_isService")
    val fdtIsService: Boolean = false,
    @SerializedName("fdt_location")
    val fdtLocation: String = "",
    @SerializedName("fdt_loss")
    val fdtLoss: String = "",
    @SerializedName("fdt_name")
    val fdtName: String = "",
    @SerializedName("fdt_note")
    val fdtNote: String = "",
    @SerializedName("fdt_uuid")
    val fdtUuid: String = ""
) {
    data class CoveredFat(
        @SerializedName("fat_name")
        val fatName: String = "",
        @SerializedName("fat_image")
        val fatImage: String = "",
        @SerializedName("uuid")
        val uuid: String = ""
    )
}