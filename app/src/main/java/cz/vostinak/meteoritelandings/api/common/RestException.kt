package cz.vostinak.meteoritelandings.api.common

/**
 * Exception thrown when an error occurs during REST API call.
 * @param title Title of the error.
 * @param message Message of the error.
 * @param resultCode Result code of the error.
 */
class RestException(
    /**
     * Title of the error.
     */
    title: String,
    /**
     * Message of the error.
     */
    message: String,
    /**
     * Result code of the error.
     */
    resultCode: Int
): Exception(message) {}