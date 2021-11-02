package com.dicoding.fcmapplication.data.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("jwt")
    val jwt: String = "",
    @SerializedName("user")
    val user: User = User()
) {
    data class User(
        @SerializedName("username")
        val username: String = ""
    )
}