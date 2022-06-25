package com.dicoding.fcmapplication.utils.variables.longlat

object LongLatRegex {
    val LATITUDE_PATTERN =
        "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$".toRegex()
    val LONGITUDE_PATTERN =
        "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$".toRegex()

}