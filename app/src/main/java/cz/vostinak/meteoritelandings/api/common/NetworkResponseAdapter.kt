package cz.vostinak.meteoritelandings.api.common

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type


/**
 * Network response adapter for handling API responses
 * @param S Success response type
 * @property successBodyType Success response body type
 * @property errorBodyConverter Error response body converter
 */
class NetworkResponseAdapter<S: Any>(
    private val successBodyType: Type,
    private val errorBodyConverter: Converter<ResponseBody, GenericRestError>
): CallAdapter<S, Call<ApiResponse<S>>> {

    override fun responseType() = successBodyType

    override fun adapt(call: Call<S>): Call<ApiResponse<S>> {
        return NetworkResponseCall(call, errorBodyConverter)
    }
}
