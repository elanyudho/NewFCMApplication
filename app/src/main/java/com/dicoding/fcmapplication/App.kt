package com.dicoding.fcmapplication

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.fcmapplication.data.network.NetworkConnection
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        var networkConnection : LiveData<Boolean>? = null
    }

    override fun onCreate() {
        super.onCreate()

        networkConnection = NetworkConnection(this)
    }
}