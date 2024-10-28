package cz.vostinak.meteoritelandings.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO

/**
 * Data access object for meteorites db.
 */
@Dao
interface MeteoriteDao {

    /**
     * Insert meteorites into db.
     * @param meteorites List of meteorites to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meteorites: List<MeteoriteApiTO>)

    /**
     * Get all meteorites from db.
     * @return List of meteorites.
     */
    @Query("SELECT * FROM meteorite")
    suspend fun getAllMeteorites(): List<MeteoriteApiTO>

    /**
     * Get meteorites by distance from given coordinates.
     * @param lat Latitude.
     * @param lng Longitude.
     * @return List of meteorites.
     */
    @Query("SELECT * FROM meteorite WHERE reclat <> '' AND reclat <> '0.000000' AND reclat IS NOT NULL AND reclong <> '' AND reclong <> '0.000000' AND reclong IS NOT NULL ORDER BY ABS(reclat - :lat) + ABS(reclong - :lng) ASC")
    suspend fun getAllMeteoritesByDistance(lat: Double, lng: Double): List<MeteoriteApiTO>

    /**
     * Get meteorites within bounds.
     * @param southLat South latitude.
     * @param northLat North latitude.
     * @param westLng West longitude.
     * @param eastLng East longitude.
     * @return List of meteorites.
     */
    @Query("SELECT * FROM meteorite WHERE reclat BETWEEN :southLat AND :northLat AND reclong BETWEEN :westLng AND :eastLng")
    suspend fun getMeteoritesWithinBounds(southLat: Double, northLat: Double, westLng: Double, eastLng: Double): List<MeteoriteApiTO>

    /**
     * Get meteorites by distance from given coordinates.
     * @param lat Latitude.
     * @param lng Longitude.
     * @return List of meteorites.
     */
    @Query("SELECT * FROM meteorite WHERE reclat <> '' AND reclat <> '0.000000' AND reclat IS NOT NULL AND reclong <> '' AND reclong <> '0.000000' AND reclong IS NOT NULL ORDER BY ABS(reclat - :lat) + ABS(reclong - :lng) ASC LIMIT 200 OFFSET :offset")
    suspend fun getMeteoritesByDistance(lat: Double, lng: Double, offset: Int = 0): List<MeteoriteApiTO>

    /**
     * Count meteorites in db.
     * @return Number of meteorites.
     */
    @Query("SELECT COUNT(*) FROM meteorite")
    suspend fun countMeteorites(): Int
}