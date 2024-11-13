package cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import cz.vostinak.meteoritelandings.R
import cz.vostinak.meteoritelandings.api.common.Resource
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.ext.combineState
import cz.vostinak.meteoritelandings.ext.safeLet
import cz.vostinak.meteoritelandings.ui.gui.map.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


/**
 * Main view model for meteorite landings list.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MainRepository
): ViewModel() {

    companion object {
        /** Default page limit for meteorites list. */
        const val DEFAULT_LIMIT = 1000
    }

    /** Meteorite list state. Private mutable. */
    private val _state = MutableStateFlow<Resource<MainListState>>(Resource.Loading())
    /** Meteorite list state. Public immutable. */
    val state = _state.asStateFlow()

    /** Map screen state. Mutable private. */
    private val _mapState = MutableStateFlow(MapState())
    /** Map screen state. Immutable public. */
    val mapState = _mapState.asStateFlow()

    /** Map bounds. Mutable private. */
    private val _currentBounds: MutableStateFlow<LatLngBounds?> = MutableStateFlow(null)
    /** Map bounds. Immutable private. */
    private val currentBounds = _currentBounds.asStateFlow()

    /** List of meteorites */
    private val meteoritesList: MutableStateFlow<List<MeteoriteApiTO>?> = MutableStateFlow(emptyList())

    /** List of meteorites within bounds */
    private val _boundedMeteoritesList: StateFlow<List<MeteoriteApiTO>?> =
        meteoritesList.combineState(currentBounds) { meteoritesList, bounds ->
            if (bounds != null) {
                return@combineState meteoritesList?.filter {
                    bounds.contains(
                        LatLng(
                            it.reclat?.toDouble() ?: 0.0,
                            it.reclong?.toDouble() ?: 0.0
                        )
                    )
                }
            }
            meteoritesList
        }
    val boundedMeteoritesList = _boundedMeteoritesList

    /** Change camera event */
    private val _cameraMoveEvents = MutableSharedFlow<LatLngBounds>(extraBufferCapacity = 1)
    /** Change camera event */
    val cameraMoveEvents = _cameraMoveEvents

    /** Location if available */
    private var location: Location? = null

    fun setLocation(newLocation: Location?) {
        location = newLocation
        getMeteoritesList()
    }

    /**
     * Get meteorites list from DB.
     */
    fun getMeteoritesList() {
        location?.let {
            getMeteoritesByDistance(it.latitude, it.longitude)
        } ?: run {
            loadDbData()
        }
    }

    /**
     * Get meteorites by distance from database.
     * @param lat Latitude.
     * @param lng Longitude.
     */
    private fun getMeteoritesByDistance(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.Default) {
            _state.value = Resource.Loading()
            val meteorites = repository.getAllMeteoritesByDistance(lat, lng)
            _state.value = Resource.Success(
                MainListState(
                    meteoritesList = meteorites,
                    filter = ListFilterEnum.DISTANCE
                )
            )
        }
    }

    /**
     * Load meteorites from database.
     */
    private fun loadDbData() {
        viewModelScope.launch(Dispatchers.Default) {
            val meteorites = mutableListOf<MeteoriteApiTO>()
            meteorites.addAll(repository.loadMeteoritesFromDb())
            if(meteorites.isNotEmpty()) {
                _state.value = Resource.Success(
                    MainListState(
                        meteoritesList = meteorites,
                        filter = ListFilterEnum.DEFAULT
                    )
                )
            } else {
                _state.value = Resource.Error(
                    Exception(
                        context.getString(R.string.error_database),
                        EmptyDbThrowable()
                    )
                )
            }
        }
    }

    /**
     * Clear sync time to force sync.
     */
    fun resetSyncTime() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.resetSyncTime()
        }
    }

    /**
     * Filter list of meteorites.
     * @param filter Filter.
     */
    fun filterList(filter: ListFilterEnum) {
        viewModelScope.launch {
            _state.value = Resource.Loading()

            if(filter == ListFilterEnum.DEFAULT) {
                loadDbData()
                return@launch
            } else if(filter == ListFilterEnum.DISTANCE) {
                location?.let {
                    getMeteoritesByDistance(it.latitude, it.longitude)
                    return@launch
                } ?: run {
                    _state.value = Resource.Error(
                        Exception(
                            context.getString(R.string.error_location),
                            LocationNotAvailableThrowable()
                        )
                    )
                    return@launch
                }
            }

            val filteredList = repository.filter(filter)
            if(filteredList.isEmpty()) {
                _state.value = Resource.Error(
                    Exception(
                        context.getString(R.string.error_filter),
                        EmptyFilterThrowable()
                    )
                )
                return@launch
            } else {
                _state.value = Resource.Success(
                    MainListState(
                        meteoritesList = filteredList,
                        filter = filter,
                        errorMessage = null
                    )
                )
            }
        }
    }

    /**
     * Update state of the map screen.
     * @param newState New state.
     * */
    fun updateState(newState: MapState) {
        viewModelScope.launch {
            _mapState.value = newState
        }
    }

    /**
     * Update current bounds of the map.
     * @param newBounds New bounds.
     * */
    fun updateCurrentBounds(newBounds: LatLngBounds) {
        viewModelScope.launch {
            _currentBounds.value = newBounds

            val longitudeDiff = newBounds.northeast.longitude - newBounds.southwest.longitude
            if(longitudeDiff < 30) {
                meteoritesList.value = repository.getMeteoritesWithinBounds(newBounds)
                _mapState.update {
                    it.copy(
                        errorMessage = null
                    )
                }
            } else {
                meteoritesList.value = listOf()
                _mapState.update {
                    it.copy(
                        errorMessage = context.getString(R.string.map_error_zoom)
                    )
                }
            }
        }
    }

    /**
     * Change selected meteorite.
     * @param meteorite Selected meteorite.
     * */
    fun onChangeSelectedMeteorite(meteorite: MeteoriteApiTO?) {
        viewModelScope.launch {
            if (meteorite != null) {
                _mapState.update {
                    it.copy(
                        lastCameraPositionState = safeLet(
                            meteorite.reclat?.toDouble() ?: 0.0,
                            meteorite.reclong?.toDouble() ?: 0.0
                        ) { latitude, longitude ->
                            LatLng(latitude, longitude)
                        },
                        lastCameraPositionZoomState = null,
                        recenterMap = false,
                        selectedMeteorite = meteorite
                    )
                }
            } else {
                _mapState.update {
                    it.copy(
                        selectedMeteorite = null
                    )
                }
            }
        }
    }

    /**
     * Center map.
     * @param recenterMap Recenter map.
     */
    fun onCenterMap(recenterMap: Boolean = false) {
        viewModelScope.launch {
            mapState.value.run {
                if (lastCameraPositionZoomState == null) {
                    // if location services enabled and the last location of the device is available - display the current location
                    val shouldShowCurrentLocation = locationPermissionGranted && (selectedMeteorite == null || recenterMap)
                    // if location services disabled and no meteorite selected or recenterMap set - center on all meteorites
                    val shouldCenterOnAllMeteorites = locationPermissionGranted.not() && (selectedMeteorite == null || recenterMap)

                    if (shouldShowCurrentLocation) {
                        lastLocationState?.let { safeLastLocationState ->
                            _mapState.update {
                                it.copy(
                                    lastCameraPositionState = safeLastLocationState,
                                    lastCameraPositionZoomState = null,
                                    recenterMap = true
                                )
                            }
                        }
                    } else if (shouldCenterOnAllMeteorites) {
                        _mapState.update {
                            it.copy(cameraUpdateBounds = getBoundsOfAllMeteorites())
                        }
                    }
                } else {
                    _mapState.update {
                        it.copy(recenterMap = true)
                    }
                }
            }
        }
    }

    /**
     * Get bounds of all meteorites.
     */
    private fun getBoundsOfAllMeteorites(): LatLngBounds? {
        val meteorites = meteoritesList.value
        if (meteorites.isNullOrEmpty()) {
            return null
        }

        val builder = LatLngBounds.builder()
        for (meteorite in meteorites) {
            meteorite.reclat?.let { lat ->
                meteorite.reclong?.let { lng ->
                    builder.include(LatLng(lat.toDouble(), lng.toDouble()))
                }
            }
        }
        return builder.build()
    }

    /**
     * Update last location state.
     */
    fun updateLastLocationState() {
        viewModelScope.launch {
            _mapState.update {
                it.copy(
                    lastLocationState = if(location != null) LatLng(location!!.latitude, location!!.longitude) else LatLng(0.0, 0.0),
                    lastCameraPositionZoomState = null
                )
            }
        }
    }

    /**
     * Update location permission granted.
     * @param locationPermissionGranted Is location permission granted?
     * */
    fun updateLocationPermissionGranted(locationPermissionGranted: Boolean) {
        viewModelScope.launch {
            _mapState.update {
                it.copy(locationPermissionGranted = locationPermissionGranted)
            }
        }
    }
}