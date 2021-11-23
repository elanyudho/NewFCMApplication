package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class FatListResponse : ArrayList<FatListResponse.FatListResponseItem>(){
    data class FatListResponseItem(
        @SerializedName("fat_name")
        val fatName: String = "",
        @SerializedName("fat_image")
        val fatImage: String = "",
        @SerializedName("uuid")
        val uuid: String = "",
        @SerializedName("fat_activate_date")
        val fatActivateDate: String = "",
        @SerializedName("fat_isService")
        val fatIsService: Boolean = false,
    )
}