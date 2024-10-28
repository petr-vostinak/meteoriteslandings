package cz.vostinak.meteoritelandings.api.common.to

import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import java.io.Serializable

/** Base TO object with toString() */
class BaseTO: Serializable {

    companion object {
        private const val  TAG = "BaseTO"
    }

    val typeName: String
        @JsonIgnore
        get() = javaClass.simpleName

    override fun toString(): String {
        return ReflectionToStringBuilder.toString(this)
    }
}