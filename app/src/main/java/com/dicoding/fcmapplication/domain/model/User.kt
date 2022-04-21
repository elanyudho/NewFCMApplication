package com.dicoding.fcmapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (

    val username: String = "",
    var token:String = "",
    val isAdmin: Boolean = false,
    val isCenterAdmin: Boolean = false,
    val region: String? = ""

) : Parcelable