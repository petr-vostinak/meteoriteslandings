package cz.vostinak.meteoritelandings.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO

/**
 * Room database for meteorites.
 */
@Database(entities = [MeteoriteApiTO::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    /** Meteorite data access object. */
    abstract fun meteoriteDao(): MeteoriteDao

    companion object {
        /** Database name key */
        private const val DATABASE_NAME = "meteorite_database"

        /** Singleton instance of database */
        @Volatile private var INSTANCE: AppDatabase? = null

        /**
         * Get database instance.
         * @param context Application context.
         * @return Database instance.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}