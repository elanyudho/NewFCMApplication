package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class FdtListResponse : ArrayList<FdtListResponse.FdtListResponseItem>(){
    data class FdtListResponseItem(
        @SerializedName("fdt_image")
        val fdtImage: String = "",
        @SerializedName("fdt_name")
        val fdtName: String = "",
        @SerializedName("uuid")
        val uuid: String = "",
        @SerializedName("fdt_activate_date")
        val fdtActivateDate: String = "",
        @SerializedName("fdt_isService")
        val fdtIsService: Boolean = false,

    )
}