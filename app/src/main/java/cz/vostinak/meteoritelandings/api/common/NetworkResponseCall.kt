package cz.vostinak.meteoritelandings.api.common

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Custom Call pro [NetworkResponseAdapter]
 */
class NetworkResponseCall<S: Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, GenericRestError>
): Call<ApiResponse<S>> {

    override fun enqueue(callback: Callback<ApiResponse<S>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@NetworkResponseCall,
                            Response.success(ApiResponse.Success(body))
                        )
                    } else {
                        if (code == HttpURLConnection.HTTP_NO_CONTENT) {
                            // Response is successful but the body is null
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.SuccessNoData)
                            )
                        }
                        else {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.UnknownError(null))
                            )
                        }
                    }
                } else {
                    if(call.request().isAnnotated(TerribleApiRequest::class.java)) {
                        Response.success(ApiResponse.CustomApiError(rawBody = error?.string()))
                    }else {
                        val errorBody = when {
                            error == null -> null
                            error.contentLength() == 0L -> null
                            else -> try {
                                errorConverter.convert(error)
                            } catch (ex: Exception) {
                                null
                            }
                        }
                        if (errorBody != null) {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.ApiError(errorBody.title, errorBody.message, code))
                            )
                        } else {
                            callback.onResponse(
                                this@NetworkResponseCall,
                                Response.success(ApiResponse.UnknownError(null))
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                callback.onResponse(this@NetworkResponseCall, Response.success(ApiResponse.UnknownError(throwable)))
            }
        })
    }

    override fun clone(): Call<ApiResponse<S>> = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun execute(): Response<ApiResponse<S>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}