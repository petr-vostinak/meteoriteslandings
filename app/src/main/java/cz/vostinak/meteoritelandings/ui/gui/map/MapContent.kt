package cz.vostinak.meteoritelandings.ui.gui.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


/** Default map zoom */
const val CAMERA_ZOOM_DEFAULT = 9f
/** Close map zoom */
const val CAMERA_ZOOM_CLOSE = 12f
/** Animation duration */
const val CAMERA_ANIMATION_DURATION = 500


@OptIn(FlowPreview::class)
@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    state: MapState,
    meteorites: List<MeteoriteApiTO>? = null,
    cameraMoveEvents: MutableSharedFlow<LatLngBounds>,
    updateState: (MapState) -> Unit = {},
    onCameraMove: (LatLngBounds) -> Unit = {},
    onClickMarker: (MeteoriteApiTO) -> Unit = {},
    onMapLoaded: () -> Unit = {},
    currentMapPadding: PaddingValues = PaddingValues(0.dp)
) {
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = state.locationPermissionGranted,
                isBuildingEnabled = true,
                mapType = MapType.NORMAL
            )
        )
    }

    val lastCameraPositionState = state.lastCameraPositionState ?: state.defaultMapCenter


    val cameraPositionState = rememberCameraPositionState {
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(lastCameraPositionState, CAMERA_ZOOM_DEFAULT)
        )
    }

    LaunchedEffect(state.lastCameraPositionState, state.lastCameraPositionZoomState, state.recenterMap) {
        state.lastCameraPositionZoomState?.let {
            cameraPositionState.centerOnLocation(location = it.first, zoom = it.second)
            updateState(state.copy(recenterMap = false))
        } ?: state.lastCameraPositionState?.let { location ->
            cameraPositionState.centerOnLocation(location = location)
            updateState(state.copy(recenterMap = false))
        }
    }

    LaunchedEffect(cameraMoveEvents) {
        cameraMoveEvents
            .debounce(200)
            .collectLatest { bounds ->
                onCameraMove(bounds)
            }
    }

    LaunchedEffect(cameraPositionState.position) {
        cameraPositionState.projection?.visibleRegion?.latLngBounds?.let {
            cameraMoveEvents.tryEmit(it)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            updateState(
                state.copy(
                    lastCameraPositionState = null,
                    lastCameraPositionZoomState = cameraPositionState.position.target to cameraPositionState.position.zoom
                )
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        contentPadding = currentMapPadding,
        onMapLoaded = {
            onMapLoaded()
        }
    ) {
        meteorites?.forEach { meteorite ->
            Marker(
                state = rememberMarkerState(
                    key = meteorite.id.toString(),
                    position = LatLng(
                        meteorite.reclat?.toDouble() ?: 0.0,
                        meteorite.reclong?.toDouble() ?: 0.0
                    )
                ),
                title = meteorite.name,
                onClick = {
                    onClickMarker(meteorite)
                    false
                }
            )
        }
    }
}

/**
 * Change of camera position.
 */
private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng,
    zoom: Float = CAMERA_ZOOM_CLOSE,
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(location, zoom),
    durationMs = CAMERA_ANIMATION_DURATION
)