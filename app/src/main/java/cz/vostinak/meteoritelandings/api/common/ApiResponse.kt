package cz.vostinak.meteoritelandings.api.common

/**
 * Terrible API request annotation
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class TerribleApiRequest

/**
 * ApiResponse class for handling API responses
 */
sealed class ApiResponse<out T: Any> {
    /** Success response with body */
    data class Success<out T: Any>(val body: T): ApiResponse<T>()
    /** Success response without body */
    data object SuccessNoData: ApiResponse<Nothing>()
    /** API error response */
    data class ApiError(val title: String?, val message: String?, val code: Int): ApiResponse<Nothing>()
    /** Unknown error response */
    data class UnknownError(val error: Throwable?): ApiResponse<Nothing>()
    /** Custom error response */
    data class CustomApiError(val rawBody: String?): ApiResponse<Nothing>()
}