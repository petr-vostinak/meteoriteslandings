package cz.vostinak.meteoritelandings.ui.gui.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO


/**
 * State of the map.
 */
data class MapState(
    /** Is loading */
    val isLoading: Boolean = true,
    /** Is map loading */
    val isMapLoading: Boolean = true,
    /** Recenter map */
    val recenterMap: Boolean = false,
    /** Location permission granted */
    val locationPermissionGranted: Boolean = false,
    /** Last location state */
    val lastLocationState: LatLng? = null,
    /** Last camera position state */
    val lastCameraPositionState: LatLng? = null,
    /** Last camera position zoom state */
    val lastCameraPositionZoomState: Pair<LatLng, Float>? = null,
    /** Camera update bounds */
    val cameraUpdateBounds: LatLngBounds? = null,
    /** Selected meteorite */
    val selectedMeteorite: MeteoriteApiTO? = null,
    /** Default map center */
    val defaultMapCenter: LatLng = LatLng(0.0, 0.0),
    /** Error message */
    val errorMessage: String? = null
)
