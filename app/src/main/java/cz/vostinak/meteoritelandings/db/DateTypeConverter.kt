package cz.vostinak.meteoritelandings.db

import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


/**
 * Type converter for date.
 */
class DateTypeConverter {

    companion object{
        /** Date format */
        private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    }

    /**
     * Convert string to date.
     * @param dateString String representation of date.
     * @return Date object.
     * */
    @TypeConverter
    fun stringToDate(dateString: String): Date? {
        val formatter = SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault())
        return try {
            formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Convert date to string.
     * @param date Date object.
     * @return String representation of date.
     */
    @TypeConverter
    fun dateToString(date: Date?): String? {
        if (date == null) {
            return null
        }
        val formatter = SimpleDateFormat(DATE_FORMAT, java.util.Locale.getDefault())
        return formatter.format(date)
    }
}