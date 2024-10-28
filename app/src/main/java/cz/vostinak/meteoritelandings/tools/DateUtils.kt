package cz.vostinak.meteoritelandings.tools

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val DATE_FORMAT = "d. MMMM yyyy"

    fun formatDate(date: Date?, locale: Locale = Locale.getDefault()): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, locale)
        return if(date != null) {
            dateFormat.format(date)
        } else {
            "---"
        }
    }
}