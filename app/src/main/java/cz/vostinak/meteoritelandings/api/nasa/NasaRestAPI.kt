package cz.vostinak.meteoritelandings.api.nasa

import cz.vostinak.meteoritelandings.BuildConfig
import cz.vostinak.meteoritelandings.api.common.ApiResponse
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * NASA API for meteorite landings.
 */
interface NasaRestAPI {

    /**
     * Get meteorite landings.
     * @param limit Maximum number of meteorites to return. (Default 1000)
     * @param offset Number of meteorites to skip. (Default 0)
     * @return List of meteorites.
     */
    @GET("/resource/gh4g-9sfh.json")
    suspend fun getMeteorites(
        @Query("\$limit") limit: Int? = null,
        @Query("\$offset") offset: Int? = null,
        @Query("\$\$app_token") order: String = BuildConfig.API_APP_TOKEN
    ): ApiResponse<List<MeteoriteApiTO>>
}