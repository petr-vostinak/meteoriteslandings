package cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel

import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO


/**
 * Main list state.
 */
data class MainListState(
    /** List of meteorites */
    val meteoritesList: List<MeteoriteApiTO> = emptyList(),
    /** Error message */
    val errorMessage: String? = null,
    /** Filter */
    val filter: ListFilterEnum = ListFilterEnum.DEFAULT
)
