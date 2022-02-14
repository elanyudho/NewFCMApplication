package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class FatResponse : ArrayList<FatResponse.FatResponseItem>(){
    data class FatResponseItem(
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
        val fatNote: String = "",
        @SerializedName("fat_region")
        val fatRegion: String = "",
        @SerializedName("fat_total_core")
        val fatTotalCore: String = "",
        @SerializedName("home_covered")
        val fatHomeCovered: String = "",
        @SerializedName("id")
        val id: Int = 0
    )
}