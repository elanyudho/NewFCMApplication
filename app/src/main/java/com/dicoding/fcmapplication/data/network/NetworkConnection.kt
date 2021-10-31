package com.dicoding.fcmapplication.data.network

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context): LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val networkBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }
    }

    @Suppress("DEPRECATION")
    override fun onActive() {
        super.onActive()
        connectivityManagerCallback()
        updateConnection()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                connectivityManager?.registerDefaultNetworkCallback(networkCallback)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                lollipopNetworkRequest()
            }
            else -> {
                context.registerReceiver(
                    networkBroadcastReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        } else {
            context.unregisterReceiver(networkBroadcastReceiver)
        }

        super.onInactive()
    }

    private fun connectivityManagerCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    postValue(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    postValue(false)
                }
            }
        } else {
            throw IllegalAccessError(this.javaClass.simpleName + " should use Lollipop or higher")
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkRequest() {
        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

        connectivityManager?.registerNetworkCallback(
            requestBuilder.build(),
            networkCallback
        )
    }

    @Suppress("DEPRECATION")
    private fun updateConnection() {
        val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
        postValue(activeNetwork?.isAvailable == true)
    }
}

interface InternetSignalHandler {
    fun onConnected()
    fun onDisconnected()
}