package cz.vostinak.meteoritelandings.api.common.to

import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import java.io.Serializable

/** Base TO object with toString() */
class BaseTO: Serializable {

    override fun toString(): String {
        return ReflectionToStringBuilder.toString(this)
    }
}