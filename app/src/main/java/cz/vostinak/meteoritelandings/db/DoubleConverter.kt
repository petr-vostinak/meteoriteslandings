package cz.vostinak.meteoritelandings.db

import androidx.room.TypeConverter


/**
 * Type converter for double.
 */
class DoubleConverter {

    /**
     * Convert string to date.
     * @param string String representation of double.
     * @return Double object.
     * */
    @TypeConverter
    fun stringToDouble(string: String?): Double? {
        return string?.toDouble()
    }

    /**
     * Convert double to string.
     * @param double Double object.
     * @return String representation of double.
     */
    @TypeConverter
    fun doubleToString(double: Double?): String? {
        return double.toString()
    }
}