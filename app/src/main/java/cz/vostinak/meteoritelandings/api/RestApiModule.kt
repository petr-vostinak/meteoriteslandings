package cz.vostinak.meteoritelandings.api

import cz.vostinak.meteoritelandings.api.nasa.NasaRestAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestApiModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpClient

    @Provides
    @BaseUrl
    fun provideBaseUrl() = "https://data.nasa.gov"

    @Singleton
    @Provides
    @OkHttpClient
    fun provideOkHttpClient(): okhttp3.OkHttpClient = RestApi.httpClient

    @Singleton
    @Provides
    fun nasaApi(): NasaRestAPI {
        return RestApi.restAdapter.create(NasaRestAPI::class.java)
    }
}