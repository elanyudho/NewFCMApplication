package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class RepairListResponse : ArrayList<RepairListResponse.RepairListResponseItem>(){
    data class RepairListResponseItem(
        @SerializedName("device_image")
        val deviceImage: String = "",
        @SerializedName("device_name")
        val deviceName: String = "",
        @SerializedName("device_note")
        val deviceNote: String = "",
        @SerializedName("uuid")
        val uuid: String = ""
    )
}