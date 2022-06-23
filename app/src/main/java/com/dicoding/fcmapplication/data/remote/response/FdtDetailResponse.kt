package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

data class FdtDetailResponse(
    @SerializedName("fat_covered_lists")
    val fatCoveredLists: List<FatCoveredLists> = listOf(),
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
    @SerializedName("updated_at")
    val updatedAt: String = ""

) {
    data class FatCoveredLists(

        @SerializedName("fat_activated")
        val fatActivated: String = "",
        @SerializedName("fat_backup_core")
        val fatBackupCore: String = "",
        @SerializedName("fat_core_used")
        val fatCoreUsed: String = "",
        @SerializedName("fat_in_repair")
        val fatInRepair: Boolean = false,
        @SerializedName("fat_location")
        val fatLocation: String = "",
        @SerializedName("fat_loss")
        val fatLoss: String = "",
        @SerializedName("fat_name")
        val fatName: String = "",
        @SerializedName("fat_note")
        val fatNote: String? = "",
        @SerializedName("fat_region")
        val fatRegion: String = "",
        @SerializedName("fat_total_core")
        val fatTotalCore: String = "",
        @SerializedName("home_covered")
        val homeCovered: String = "",
        @SerializedName("id")
        val id: Int = 0,
    )
}