package cz.vostinak.meteoritelandings.tools

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class for date formatting.
 */
object DateUtils {

    /** Date format */
    private const val DATE_FORMAT = "d. MMMM yyyy"

    /**
     * Format date to string.
     * @param date Date object.
     * @param locale Locale for formatting.
     * @return Formatted date string.
     */
    fun formatDate(date: Date?, locale: Locale = Locale.getDefault()): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
        return if(date != null) {
            dateFormat.format(date)
        } else {
            "---"
        }
    }
}