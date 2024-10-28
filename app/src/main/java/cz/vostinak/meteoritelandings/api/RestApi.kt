package cz.vostinak.meteoritelandings.api

import com.google.gson.GsonBuilder
import cz.vostinak.meteoritelandings.BuildConfig
import cz.vostinak.meteoritelandings.api.common.NetworkResponseAdapterFactory
import cz.vostinak.meteoritelandings.api.common.to.RequestTOHolder
import cz.vostinak.meteoritelandings.api.nasa.DateDeserializer
import cz.vostinak.meteoritelandings.api.nasa.NasaRestAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit


/**
 * Rest API object.
 */
object RestApi {

    private var nasaRestAPIfield: NasaRestAPI? = null
    /** Nasa API */
    @JvmStatic val nasaRestAPI: NasaRestAPI
        get() {
            nasaRestAPIfield = nasaRestAPIfield ?: restAdapter.create(NasaRestAPI::class.java)
            return nasaRestAPIfield!!
        }

    /** Sdilena instance okHttp klienta */
    private var httpClientField: OkHttpClient? = null

    /** Pouze synchronizacni lock pro okHttp klienta */
    private val httpClientLock = Any()

    private var gsonConverterField: GsonConverterFactory? = null
    /** Gson konvertor */
    private val gsonConverter: GsonConverterFactory
        get() {
            gsonConverterField = gsonConverterField ?: GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Date::class.java, DateDeserializer())
                    .create()
            )
            return gsonConverterField!!
        }

    @JvmStatic
    val httpClient: OkHttpClient
        get() {
            // okHttp optimalizace
            synchronized(httpClientLock) {

                httpClientField?.let {
                    return it
                }

                val httpClientBuilder = OkHttpClient.Builder()

                httpClientBuilder.addInterceptor( Interceptor { chain ->
                    val builder = chain.request().newBuilder()

                    val request = builder
                        // .header("header_name", "header_value")
                        .build()

                    val response = chain.withReadTimeout(30, TimeUnit.SECONDS).proceed(request)

                    response
                })

                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addInterceptor(loggingInterceptor)

                httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
                httpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)

                if(BuildConfig.DEBUG) {
                    httpClientBuilder.addInterceptor(LogRestApiForCrashInterceptor())
                }

                httpClientBuilder.build().let {
                    httpClientField = it
                    return it
                }
            }
        }

    @JvmStatic
    val restAdapter: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(RestApiModule.provideBaseUrl())
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .client(httpClient)
            .callFactory { request ->
                val request2 = request.newBuilder()
                    .tag(RequestTOHolder::class.java, RequestTOHolder())
                    .build()
                httpClient.newCall(request2)
            }
            .build()
}