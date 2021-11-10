package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class FatListResponse : ArrayList<FatListResponse.FatListResponseItem>(){
    data class FatListResponseItem(
        @SerializedName("fat_image")
        val fatImage: List<FatImage> = listOf(),
        @SerializedName("fat_name")
        val fatName: String = "",
        @SerializedName("uuid")
        val uuid: String = ""
    ) {
        data class FatImage(
            @SerializedName("url")
            val url: String = ""
        )
    }
}