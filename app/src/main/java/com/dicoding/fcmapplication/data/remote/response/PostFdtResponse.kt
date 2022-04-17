package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

data class PostFdtResponse(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("fat_covered_lists")
    val fatCoveredLists: List<Any> = listOf(),
    @SerializedName("fdt_activated")
    val fdtActivated: String = "",
    @SerializedName("fdt_backup_core")
    val fdtBackupCore: String = "",
    @SerializedName("fdt_core_used")
    val fdtCoreUsed: String = "",
    @SerializedName("fdt_in_repair")
    val fdtInRepair: Boolean = false,
    @SerializedName("fdt_location")
    val fdtLocation: String = "",
    @SerializedName("fdt_loss")
    val fdtLoss: String = "",
    @SerializedName("fdt_name")
    val fdtName: String = "",
    @SerializedName("fdt_note")
    val fdtNote: String = "",
    @SerializedName("fdt_region")
    val fdtRegion: String = "",
    @SerializedName("fdt_total_core")
    val fdtTotalCore: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("published_at")
    val publishedAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)