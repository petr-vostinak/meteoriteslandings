package cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel

import android.content.SharedPreferences
import cz.vostinak.meteoritelandings.api.common.ApiResponse
import cz.vostinak.meteoritelandings.api.common.Resource
import cz.vostinak.meteoritelandings.api.nasa.NasaRestAPI
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.db.MeteoriteDao
import java.util.Date
import javax.inject.Inject


/**
 * Splash repository for meteorite landings.
 */
class SplashRepository @Inject constructor(
    private val nasaRestAPI: NasaRestAPI,
    private val preferences: SharedPreferences,
    private val meteoriteDao: MeteoriteDao
) {

    companion object {
        /** Default limit for meteorites API sync is 24 hours */
        const val SYNC_TIMEOUT = 1000 * 60 * 60 * 24
        /** Key for sync time in SharedPreferences */
        const val SYNC_KEY = "sync_key"
    }

    /**
     * Load meteorites data from NASA API
     * @param limit Maximum number of meteorites to return. (Default 1000)
     * @param offset Number of meteorites to skip. (Default 0)
     * @return List of meteorites.
     */
    suspend fun loadMeteoritesFromApi(limit: Int? = null, offset: Int? = null): Resource<List<MeteoriteApiTO>> {
        val response = nasaRestAPI.getMeteorites(limit, offset)
        if(response is ApiResponse.Success) {
            return Resource.Success(response.body)
        }
        return Resource.Error(null)
    }

    /**
     * Save meteorites data to database.
     * @param meteorites List of meteorites to save.
     */
    suspend fun saveMeteoritesToDb(meteorites: List<MeteoriteApiTO>) {
        meteoriteDao.insertAll(meteorites)
    }

    /**
     * Check if it is time to sync meteorites data from NASA API.
     * @return True if it is time to sync data.
     */
    fun isSyncTime(): Boolean {
        val now = Date().time
        val lastSyncTime = preferences.getLong(SYNC_KEY, 0)

        return now - lastSyncTime > SYNC_TIMEOUT
    }

    /**
     * Mark time of last sync.
     */
    fun markSyncTime() {
        preferences.edit().putLong(SYNC_KEY, Date().time).apply()
    }

    /**
     * Check if database is empty.
     * @return True if database is empty.
     */
    suspend fun isDbEmpty(): Boolean {
        return meteoriteDao.countMeteorites() == 0
    }
}