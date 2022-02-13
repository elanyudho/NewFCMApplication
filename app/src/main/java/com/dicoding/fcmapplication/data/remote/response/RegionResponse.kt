package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

class RegionResponse : ArrayList<RegionResponse.RegionResponseItem>(){
    data class RegionResponseItem(
        @SerializedName("region_name")
        val regionName: String = ""
    )
}