package cz.vostinak.meteoritelandings.ui.gui.hilt

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Shared preferences module.
 */
@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    companion object {
        /*** Shared preferences name. */
        const val SHARED_PREFERENCES_NAME = "meteorite_landings_preferences"
    }

    /**
     * Provide shared preferences.
     */
    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}