package cz.vostinak.meteoritelandings.ui.gui.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.MainViewModel


/**
 * Meteorite map screen.
 * @param viewModel Main view model
 * @param openMapsWithCoordinates Open map with coordinates
 * @param onNavigateToRoute Navigate to route
 * @param getLastLocation Get last location
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MainViewModel,
    openMapsWithCoordinates: (LatLng) -> Unit = {},
    onNavigateToRoute: (String) -> Unit = {},
    getLastLocation: () -> Unit = {},
) {
    val state by viewModel.mapState.collectAsState()
    val boundedMeteoritesList by viewModel.boundedMeteoritesList.collectAsState()

    LaunchedEffect(state.locationPermissionGranted, state.isLoading.not()) {
        if (state.locationPermissionGranted && state.lastCameraPositionZoomState == null) {
            getLastLocation()
        } else {
            viewModel.updateState(state.copy(lastLocationState = null))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Meteorites Map") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            MapContent(
                state = state,
                meteorites = boundedMeteoritesList,
                cameraMoveEvents = viewModel.cameraMoveEvents,
                updateState = viewModel::updateState,
                onCameraMove = viewModel::updateCurrentBounds,
                onClickMarker = viewModel::onChangeSelectedMeteorite,
                onMapLoaded = {
                    viewModel.updateState(state.copy(isMapLoading = false))
                    viewModel.onCenterMap()
                }
            )

            AnimatedVisibility(!state.errorMessage.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Gray)
                        .padding(16.dp),
                    text = state.errorMessage ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}