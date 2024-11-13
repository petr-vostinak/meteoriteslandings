package cz.vostinak.meteoritelandings.ui.gui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import cz.vostinak.meteoritelandings.BuildConfig
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * Single app activity
 */
@AndroidEntryPoint
class SingleActivity: ComponentActivity() {

    /** Fused location client */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /** Location permissions */
    private val locationPermissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    /** View model */
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkLocationPermission())
            viewModel.updateLocationPermissionGranted(locationPermissionGranted = true)
        else
            requestLocationLauncher.launch(locationPermissions)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        enableEdgeToEdge()

        setContent {
            MainNavHost(
                mainViewModel = viewModel,
                onFinish = { finish() },
                onNavigate = { meteorite -> navigateToLanding(meteorite) }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestLocationPermission()
    }

    /**
     * Navigate to landing
     */
    private fun navigateToLanding(meteorite: MeteoriteApiTO) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:0,0?q=${meteorite.reclat},${meteorite.reclong} (${meteorite.name})")
        )
        startActivity(intent)
    }

    /**
     * Check and request permission
     */
    private fun checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, get location
            getLastKnownLocation()
        } else {
            // Request location permission
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Handle permission result
     */
    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, get location
                viewModel.updateLocationPermissionGranted(locationPermissionGranted = true)
                getLastKnownLocation()
            } else {
                // Permission denied
                viewModel.updateLocationPermissionGranted(locationPermissionGranted = false)
                if(BuildConfig.DEBUG) {
                    Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.setLocation(null)
            }
        }

    /**
     * Get the user's last known location
     */
    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(BuildConfig.DEBUG) {
                if (location != null) {
                    // Handle location object (e.g., show latitude and longitude)
                    Toast.makeText(
                        this,
                        "Location: ${location.latitude}, ${location.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Location not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            viewModel.setLocation(location)
        }
    }

    /**
     * Check if location permission is granted
     */
    private fun checkLocationPermission(): Boolean {
        locationPermissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    /**
     * Request location launcher
     */
    @SuppressLint("MissingPermission")
    private val requestLocationLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.any { it.value && it.key in locationPermissions }) {
            viewModel.updateLocationPermissionGranted(locationPermissionGranted = true)
            viewModel.updateLastLocationState()
        }
    }
}