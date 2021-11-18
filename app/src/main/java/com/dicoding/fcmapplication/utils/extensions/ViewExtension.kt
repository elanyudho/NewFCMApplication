package com.dicoding.fcmapplication.utils.extensions

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.utils.version.VersionUtil.hasSdkAboveEqual
import com.shashank.sony.fancytoastlib.FancyToast

fun Context.setStatusBar(color: Int, activity: Activity?) {
    activity?.window?.statusBarColor = ContextCompat.getColor(this, color)
    if (hasSdkAboveEqual(Build.VERSION_CODES.R)) {
        if(ContextCompat.getColor(this, color).isColorDark()){
            activity?.window?.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            activity?.window?.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }
    else {
        val theme = if (ContextCompat.getColor(this, color).isColorDark()) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity?.window?.decorView?.systemUiVisibility = theme
        //Timber.d("${ContextCompat.getColor(this, color).isColorDark()}, ${ContextCompat.getColor(this, color)}, ${theme}")
    }

}

fun ImageView.glide(view: View, image: Any) {
    try {
        Glide.with(view)
            .load(image)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(this)
    } catch (ignored: Throwable) {
    }
}

fun ImageView.glide(context: Context, image: Any) {
    try {
        Glide.with(context)
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

fun enable(view: View) {
    with(view) {
        isFocusable = true
        isClickable = true
        isEnabled = true
    }
}

fun disable(view: View) {
    with(view) {
        isFocusable = false
        isClickable = false
        isEnabled = false
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}