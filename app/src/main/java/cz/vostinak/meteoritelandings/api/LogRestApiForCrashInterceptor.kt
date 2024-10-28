package cz.vostinak.meteoritelandings.api

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Interceptor for logging rest api calls for crashlytics.
 */
class LogRestApiForCrashInterceptor: Interceptor {

    companion object {
        /** Tag for logging. */
        private const val TAG = "LogRestApiForCrashListener"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        try {
            var url = request.url.toString().trim()
            if (url.startsWith(RestApiModule.provideBaseUrl()) && url.length > RestApiModule.provideBaseUrl().length) {
                url = url.substring(RestApiModule.provideBaseUrl().length)
            }

            val methodType = request.method
            val responseCode = response.code

            Firebase.crashlytics.log("Call $methodType $url with response code $responseCode")
        } catch (e: Exception) {
            Log.e(TAG, "intercept(): ", e)
        }

        return response
    }

}