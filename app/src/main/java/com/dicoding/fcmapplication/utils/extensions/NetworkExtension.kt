package com.dicoding.fcmapplication.utils.extensions

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

fun <T> Response<T>.isTotallySuccess(): Boolean {
    return this.isSuccessful && this.body() != null
}

fun <T> Response<T>.hasEmptyBody(): Boolean {
    return this.isSuccessful && this.body() == null
}

fun String.toMultipartForm(): RequestBody {
    return this.toRequestBody(MultipartBody.FORM)
}