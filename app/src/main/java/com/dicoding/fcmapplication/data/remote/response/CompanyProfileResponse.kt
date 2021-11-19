package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

data class CompanyProfileResponse(
    @SerializedName("company_banner")
    val companyBanner: String = "",
    @SerializedName("company_detail")
    val companyDetail: String = "",
    @SerializedName("company_image")
    val companyImage: String = "",
    @SerializedName("company_name")
    val companyName: String = ""
)