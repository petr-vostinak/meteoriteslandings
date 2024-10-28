package cz.vostinak.meteoritelandings.ui.gui.hilt

import android.content.Context
import cz.vostinak.meteoritelandings.db.AppDatabase
import cz.vostinak.meteoritelandings.db.MeteoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Database module.
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /**
     * Provide meteorite data access object.
     */
    @Singleton
    @Provides
    fun provideMeteoriteDao(@ApplicationContext context: Context): MeteoriteDao {
        return AppDatabase.getDatabase(context).meteoriteDao()
    }
}