package cz.vostinak.meteoritelandings.api.common

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * Network response adapter factory for handling API responses
 */
class NetworkResponseAdapterFactory: CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if(Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            "Return type must be parameterized as Call<ApiResponse<Foo>> or Call<ApiResponse<out Foo>>"
        }

        val responseType = getParameterUpperBound(0, returnType)

        if(getRawType(responseType) != ApiResponse::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            "Response must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<GenericRestError>(null, GenericRestError::class.java, annotations)

        return NetworkResponseAdapter<Any>(successBodyType, errorBodyConverter)
    }
}