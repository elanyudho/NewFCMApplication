package com.dicoding.fcmapplication.ui.location

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityLocationBinding
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {

    private lateinit var dataLocation: String
    private lateinit var deviceName: String
    private var dataType by Delegates.notNull<Int>()

    private lateinit var binding: ActivityLocationBinding

    private var mapboxMap: MapboxMap? = null

    private  var symbolManager: SymbolManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataLocation = intent.extras?.getString(EXTRA_COORDINATE) ?: ""
        deviceName = intent.extras?.getString(EXTRA_NAME) ?: ""
        dataType = intent.extras?.getInt(EXTRA_TYPE) as Int

        binding.btnBack.setOnClickListener { onBackPressed() }

        when(dataType){
            1 -> {
                binding.tvTitleHeader.text = getString(R.string.fdt_location)
            }
            2 -> {
                binding.tvTitleHeader.text = getString(R.string.fat_location)
            }
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                /*symbolManager = SymbolManager(binding.mapView, mapboxMap, style)
                symbolManager?.iconAllowOverlap = true

                style.addImage(
                    ICON_ID,
                    BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_location_on_24)
                )

                showPosition(dataLocation, deviceName)*/
            }
        }

    }

    private fun showPosition(location: String, deviceName: String) {
        val latLng = location.split(",")
        val locationDevice = LatLng(latLng[0].toDouble(),latLng[1].toDouble())
        symbolManager?.create(
            SymbolOptions()
                .withLatLng(LatLng(locationDevice.latitude,locationDevice.longitude))
                .withIconImage(ICON_ID)
                .withIconSize(1.5f)
                .withIconOffset(arrayOf(0f, -1.5f))
                .withTextField(deviceName)
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(arrayOf(0f, 1.5f))
                .withDraggable(false)
        )

        mapboxMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(locationDevice, 8.0))
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    companion object {

        const val EXTRA_COORDINATE = "coordinate location"

        const val EXTRA_TYPE = "type device"

        const val EXTRA_NAME = "name device"

        private const val ICON_ID = "ICON_ID"

    }
}