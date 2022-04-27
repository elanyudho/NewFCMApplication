package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class RegionAdminResponse : ArrayList<RegionAdminResponse.RegionAdminResponseItem>(){
    data class RegionAdminResponseItem(
        @SerializedName("admin_email")
        val adminEmail: String = "",
        @SerializedName("admin_image")
        val adminImage: List<AdminImage> = listOf(),
        @SerializedName("admin_name")
        val adminName: String = "",
        @SerializedName("admin_phone")
        val adminPhone: String = "",
        @SerializedName("admin_positon")
        val adminPositon: String = "",
        @SerializedName("admin_region")
        val adminRegion: String = "",
        @SerializedName("id")
        val id: Int = 0
    ) {
        data class AdminImage(
            @SerializedName("url")
            val url: String = ""
        )
    }
}