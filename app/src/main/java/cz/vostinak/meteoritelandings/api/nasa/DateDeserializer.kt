package cz.vostinak.meteoritelandings.api.nasa

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Date deserializer for API.
 */
class DateDeserializer : JsonDeserializer<Date> {

    companion object {
        /** Format of date from API */
        private const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        /** Timezone id */
        private const val TIMEZONE = "GMT"
    }

    override fun deserialize(element: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date? {
        val date = element.asString
        val format = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone(TIMEZONE)

        return try {
            format.parse(date)
        } catch (exp: ParseException) {
            System.err.println(exp.message)
            null
        }
    }
}