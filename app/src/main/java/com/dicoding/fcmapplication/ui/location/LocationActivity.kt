package com.dicoding.fcmapplication.ui.location

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.fcmapplication.R
import com.dicoding.fcmapplication.databinding.ActivityLocationBinding
import com.dicoding.fcmapplication.utils.extensions.fancyToast
import com.dicoding.fcmapplication.utils.extensions.gone
import com.dicoding.fcmapplication.utils.extensions.visible
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newCameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception
import kotlin.properties.Delegates

@AndroidEntryPoint
class LocationActivity : AppCompatActivity() {

    private lateinit var dataLocation: String
    private lateinit var deviceName: String
    private var dataType by Delegates.notNull<Int>()

    private lateinit var binding: ActivityLocationBinding

    private var mapboxMap: MapboxMap? = null

    private lateinit var locationComponent: LocationComponent

    private lateinit var mylocation: LatLng

    private var navigationMapRoute: NavigationMapRoute? = null

    private var currentRoute: DirectionsRoute? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataLocation = intent.extras?.getString(EXTRA_COORDINATE) ?: ""
        deviceName = intent.extras?.getString(EXTRA_NAME) ?: ""
        dataType = intent.extras?.getInt(EXTRA_TYPE) as Int

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.tvDeviceName.text = deviceName

        when (dataType) {
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
                showDeviceLocation()
                showMyLocation(style)
                setRoute()
                showNavigation()

                navigationMapRoute = NavigationMapRoute(
                    null,
                    binding.mapView,
                    mapboxMap,
                    R.style.NavigationMapRoute
                )

            }
        }

    }

    private fun showDeviceLocation() {
        val latLng = dataLocation.split(",")
        val locationDevice = LatLng(latLng[0].toDouble(), latLng[1].toDouble())
        val position = CameraPosition.Builder()
            .target(locationDevice)
            .zoom(11.0)
            .tilt(10.0)
            .bearing(5.0)
            .build()

        mapboxMap?.animateCamera(newCameraPosition(position), 5000)
        mapboxMap?.addMarker(MarkerOptions().setPosition(locationDevice).title(deviceName))
    }

    @SuppressLint("MissingPermission")
    private fun showMyLocation(style: Style) {
        val locationComponentOptions = LocationComponentOptions.builder(this)
            .pulseEnabled(true)
            .pulseColor(Color.BLUE)
            .pulseAlpha(.4f)
            .pulseInterpolator(BounceInterpolator())
            .build()
        val locationComponentActivationOptions = LocationComponentActivationOptions
            .builder(this, style)
            .locationComponentOptions(locationComponentOptions)
            .build()
        locationComponent = mapboxMap?.locationComponent!!
        locationComponent.activateLocationComponent(locationComponentActivationOptions)
        try {
            locationComponent.isLocationComponentEnabled = true
            mylocation = LatLng(
                locationComponent.lastKnownLocation?.latitude as Double,
                locationComponent.lastKnownLocation?.longitude as Double
            )
        } catch (e : Exception) {
            fancyToast(getString(R.string.error_unknown_error), FancyToast.ERROR)
            mylocation = LatLng(
                0.toDouble(),
                0.toDouble()
            )
        }

    }

    private fun setRoute() {
        val latLng = dataLocation.split(",")
        val startPoint = Point.fromLngLat(mylocation.longitude, mylocation.latitude)
        val endPoint = Point.fromLngLat(latLng[1].toDouble(), latLng[0].toDouble())
        requestRoute(startPoint, endPoint)
    }

    private fun requestRoute(originPoint: Point, endPoint: Point) {
        navigationMapRoute?.updateRouteVisibilityTo(false)
        NavigationRoute.builder(this)
            .accessToken(getString(R.string.mapbox_access_token))
            .origin(originPoint)
            .destination(endPoint)
            .build()
            .getRoute(object : retrofit2.Callback<DirectionsResponse> {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>
                ) {
                    when{
                        response.body() == null -> {
                            //fancyToast("No routes found, make sure you set the right user and access token.", FancyToast.WARNING)
                            binding.startNavigation.gone()
                            return
                        }
                        response.body()?.routes()?.size == 0 -> {
                            fancyToast("No routes found from your location to $deviceName.", FancyToast.WARNING)
                            binding.startNavigation.gone()
                        }
                        else -> {
                            currentRoute = response.body()?.routes()?.get(0)

                            navigationMapRoute?.addRoute(currentRoute)

                            binding.startNavigation.visible()
                        }
                    }
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Toast.makeText(this@LocationActivity, "Error : $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showNavigation() {
        binding.btnStartNavigation.setOnClickListener {
            val simulateRoute = true

            val options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(simulateRoute)
                .build()

            NavigationLauncher.startNavigation(this, options)
        }
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

    }
}