package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class FdtListResponse : ArrayList<FdtListResponse.FdtListResponseItem>(){
    data class FdtListResponseItem(
        @SerializedName("fdt_image")
        val fdtImage: List<FdtImage> = listOf(),
        @SerializedName("fdt_name")
        val fdtName: String = "",
        @SerializedName("uuid")
        val uuid: String = ""
    ) {
        data class FdtImage(
            @SerializedName("url")
            val url: String = ""
        )
    }
}