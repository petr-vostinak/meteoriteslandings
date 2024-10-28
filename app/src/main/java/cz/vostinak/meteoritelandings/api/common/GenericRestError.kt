package cz.vostinak.meteoritelandings.api.common

/**
 * Generic REST error response
 * @param message Error message
 * @param title Error title
 * @param code Error code
 */
data class GenericRestError(
    /** Error message */
    val message: String? = null,
    /** Error title */
    val title: String? = null,
    /** Error code */
    val code: Int? = null
)
