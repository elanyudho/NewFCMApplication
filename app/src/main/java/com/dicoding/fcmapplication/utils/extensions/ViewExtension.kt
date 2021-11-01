package com.dicoding.fcmapplication.utils.extensions

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.fcmapplication.R
import com.shashank.sony.fancytoastlib.FancyToast


fun ImageView.glide(view: View, image: Any) {
    try {
        Glide.with(view)
            .load(image)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(this)
    } catch (ignored: Throwable) {
    }
}

fun Context.fancyToast(message: String,type:Int){
    FancyToast.makeText(this,message,
        FancyToast.LENGTH_SHORT,
        type,false)
        .show()
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}