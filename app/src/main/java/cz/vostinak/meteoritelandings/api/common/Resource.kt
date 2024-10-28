package cz.vostinak.meteoritelandings.api.common

import java.lang.Exception

/**
 * Generic resource class for handling API responses
 * @param T response type
 */
sealed class Resource<out T: Any> {

    data class Success<out T: Any>(val data: T?): Resource<T>()

    data class Error(val exception: Exception? = null): Resource<Nothing>()

    data class Loading(val data: Any? = null): Resource<Nothing>()
}