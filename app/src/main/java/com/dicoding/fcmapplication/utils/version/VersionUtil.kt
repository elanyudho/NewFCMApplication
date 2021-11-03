package com.dicoding.fcmapplication.utils.version

import android.os.Build

object VersionUtil {

    fun hasSdkAboveEqual(sdk: Int): Boolean {
        return Build.VERSION.SDK_INT >= sdk
    }

    fun hasSdkBelowEqual(sdk: Int): Boolean {
        return Build.VERSION.SDK_INT <= sdk
    }

    fun sdkEqual(sdk: Int): Boolean {
        return Build.VERSION.SDK_INT == sdk
    }

    fun sdkHigherThanAndroidR(): Boolean {
        return hasSdkAboveEqual(Build.VERSION_CODES.R)
    }

}