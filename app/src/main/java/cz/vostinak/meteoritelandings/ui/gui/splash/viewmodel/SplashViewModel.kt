package cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vostinak.meteoritelandings.api.common.Resource
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.MainViewModel.Companion.DEFAULT_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Splash view model for meteorite landings.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: SplashRepository
): ViewModel() {

    companion object {
        /** Default limit for splash screen visibility. */
        const val TIME_LIMIT: Long = 1000
    }

    /** Screen state. Mutable private. */
    private val _state = MutableStateFlow(SplashScreenState())
    /** Screen state. Immutable public. */
    val state = _state.asStateFlow()

    /**
     * Get meteorites list from DB or API.
     */
    fun getMeteoritesList() {
        _state.value = SplashScreenState(
            syncDone = false,
            error = SyncStateEnum.SYNC_IN_PROGRESS
        )

        if(repository.isSyncTime()) {
            downloadApiData()
        } else {
            // Delay splash screen to prevent blinking
            Handler(Looper.getMainLooper()).postDelayed({
                _state.value = SplashScreenState(
                    syncDone = true,
                    error = SyncStateEnum.SYNC_DONE
                )
            }, TIME_LIMIT)

        }
    }

    /**
     * Get meteorites list from API.
     */
    private fun downloadApiData() {
        viewModelScope.launch(Dispatchers.Default) {
            loadData(DEFAULT_LIMIT, 0)
        }
    }

    /**
     * Load meteorites data from NASA API
     * @param limit Maximum number of meteorites to return. (Default 1000)
     * @param offset Number of meteorites to skip. (Default 0)
     */
    private suspend fun loadData(limit: Int = DEFAULT_LIMIT, offset: Int = 0) {
        val response = repository.loadMeteoritesFromApi(limit, offset)

        if(response is Resource.Success) {
            if(response.data?.isNotEmpty() == true) {
                if(response.data.size < DEFAULT_LIMIT) { // Last page loaded
                    saveMeteoritesToDb(response.data)
                } else { // Load next page
                    saveMeteoritesToDb(response.data)

                    if(offset == 0) {
                        repository.markSyncTime()
                        _state.value = SplashScreenState(
                            syncDone = true,
                            error = SyncStateEnum.SYNC_DONE
                        )
                    }
                    loadData(DEFAULT_LIMIT, offset + response.data.size)
                }
            }
        } else {
            if(repository.isDbEmpty()) {
                _state.value = SplashScreenState(
                    syncDone = false,
                    error = SyncStateEnum.ERROR_NO_LOCAL_DATA
                )
            } else {
                _state.value = SplashScreenState(
                    syncDone = false,
                    error = SyncStateEnum.ERROR_LOCAL_DATA
                )
            }
        }
    }

    /**
     * Save meteorites to database.
     */
    private fun saveMeteoritesToDb(meteorites: List<MeteoriteApiTO>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.saveMeteoritesToDb(meteorites)
        }
    }

}