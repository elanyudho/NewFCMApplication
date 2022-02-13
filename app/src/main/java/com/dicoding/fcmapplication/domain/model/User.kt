package com.dicoding.fcmapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (

    val username: String?,
    val token:String?,
    val isAdmin: Boolean?,
    val region: String?

) : Parcelable