package cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel

import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLngBounds
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.db.MeteoriteDao
import cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel.SplashRepository
import javax.inject.Inject


/**
 * Main repository for meteorite landings list.
 */
class MainRepository @Inject constructor(
    private val preferences: SharedPreferences,
    private val meteoriteDao: MeteoriteDao
) {
    /**
     * Load meteorites data from database.
     * @return List of meteorites.
     */
    suspend fun loadMeteoritesFromDb(): List<MeteoriteApiTO> {
        return meteoriteDao.getAllMeteorites()
    }

    /**
     * Get meteorites by distance from database.
     * @param lat Latitude.
     * @param lng Longitude.
     * @return List of meteorites.
     */
    suspend fun getAllMeteoritesByDistance(lat: Double, lng: Double): List<MeteoriteApiTO> {
        return meteoriteDao.getAllMeteoritesByDistance(lat, lng)
    }

    /**
     * Get meteorites within bounds.
     * @param bounds Bounds.
     * @return List of meteorites.
     */
    suspend fun getMeteoritesWithinBounds(bounds: LatLngBounds): List<MeteoriteApiTO> {
        return meteoriteDao.getMeteoritesWithinBounds(bounds.southwest.latitude, bounds.northeast.latitude, bounds.southwest.longitude, bounds.northeast.longitude)
    }

    /**
     * Clear sync time to force sync.
     */
    fun resetSyncTime() {
        preferences.edit().remove(SplashRepository.SYNC_KEY).apply()
    }

    /**
     * Filter list of meteorites.
     * @param filter Filter.
     * @return Filtered list of meteorites.
     */
    suspend fun filter(filter: ListFilterEnum): List<MeteoriteApiTO> {
        val bounds = FilterBoundsUtils.getBoundsByFilter(filter)
        return if(bounds != null) {
            meteoriteDao.getMeteoritesWithinBounds(
                bounds.southwest.latitude,
                bounds.northeast.latitude,
                bounds.southwest.longitude,
                bounds.northeast.longitude
            )
        } else {
            meteoriteDao.getAllMeteorites()
        }
    }
}