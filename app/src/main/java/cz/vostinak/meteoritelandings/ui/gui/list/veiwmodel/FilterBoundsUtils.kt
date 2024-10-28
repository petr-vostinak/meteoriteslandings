package cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object FilterBoundsUtils {

    private val europeBounds = LatLngBounds(
        LatLng(34.0, -25.0),   // Southwest corner
        LatLng(71.0, 50.0)    // Northeast corner
    )
    private val asiaBounds = LatLngBounds(
        LatLng(1.0, 26.0),   // Southwest corner
        LatLng(81.0, 180.0)  // Northeast corner
    )
    private val africaBounds = LatLngBounds(
        LatLng(-34.833333, -51.0), // Southwest corner
        LatLng(37.5, 51.0)         // Northeast corner
    )
    private val northAmericaBounds = LatLngBounds(
        LatLng(15.0, -168.0), // Southwest corner
        LatLng(49.0, -53.0)   // Northeast corner
    )
    private val southAmericaBounds = LatLngBounds(
        LatLng(-56.0, -81.0), // Southwest corner
        LatLng(13.0, -34.0)   // Northeast corner
    )
    private val australiaBounds = LatLngBounds(
        LatLng(-43.63459, 113.33895), // Southwest corner
        LatLng(-10.66818, 153.56948)  // Northeast corner
    )
    private val antarcticaBounds = LatLngBounds(
        LatLng(-90.0, -180.0), // Southwest corner
        LatLng(-60.0, 180.0)   // Northeast corner
    )

    /**
     * Get bounds by filter.
     * @param filter Filter.
     * @return Bounds.
     */
    fun getBoundsByFilter(filter: ListFilterEnum): LatLngBounds? {
        return when(filter) {
            ListFilterEnum.EUROPE -> europeBounds
            ListFilterEnum.ASIA -> asiaBounds
            ListFilterEnum.AFRICA -> africaBounds
            ListFilterEnum.NORTH_AMERICA -> northAmericaBounds
            ListFilterEnum.SOUTH_AMERICA -> southAmericaBounds
            ListFilterEnum.AUSTRALIA -> australiaBounds
            ListFilterEnum.ANTARCTICA -> antarcticaBounds
            else -> null
        }
    }

}