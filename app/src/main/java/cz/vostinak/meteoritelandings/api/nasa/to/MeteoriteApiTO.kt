package cz.vostinak.meteoritelandings.api.nasa.to

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cz.vostinak.meteoritelandings.db.DateTypeConverter
import java.util.Date


/**
 * Transfer object for meteorite fall data from NASA API
 */
@Entity(tableName = "meteorite")
data class MeteoriteApiTO(
    /** Unique identifier */
    @PrimaryKey
    val id: Long,
    /** Name of location */
    val name: String,
    /** Name type */
    val nametype: String,
    /** Reclassification */
    val recclass: String,
    /** Mass of meteorite in grams */
    val mass: String?,
    /** Fall type */
    val fall: String,
    /** Year of fall */
    @TypeConverters(DateTypeConverter::class)
    val year: Date?,
    /** Latitude of fall */
    @TypeConverters(DateTypeConverter::class)
    val reclat: Double?,
    /** Longitude of fall */
    @TypeConverters(DateTypeConverter::class)
    val reclong: Double?
)
