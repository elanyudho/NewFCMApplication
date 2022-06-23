package com.dicoding.fcmapplication.utils.extensions

import android.util.Patterns
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.convertDate(): String {
    //change to local time
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val localTime: String = LocalDateTime.parse(this, formatter)
        .atOffset(ZoneOffset.UTC)
        .atZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        .format(formatter)

    //change to format
    val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val destFormat = SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a")
    val convertedDate = sourceFormat.parse(localTime)
    return "${getDayOfTheWeek(this)}, ${destFormat.format(convertedDate)}"
}

fun getDayOfTheWeek(date: String): String {
    val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val dt1 = sourceFormat.parse(date)
    val dateFormat: DateFormat = SimpleDateFormat("EEEE")
    return dateFormat.format(dt1)
}