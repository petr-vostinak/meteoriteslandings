package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.FilterChip
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.ListFilterEnum
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/**
 * List of filter chips.
 */
private val chipsList = listOf(
    FilterChip("Distance", ListFilterEnum.DISTANCE),
    FilterChip("Europe", ListFilterEnum.EUROPE),
    FilterChip("Asia", ListFilterEnum.ASIA),
    FilterChip("Africa", ListFilterEnum.AFRICA),
    FilterChip("North America", ListFilterEnum.NORTH_AMERICA),
    FilterChip("South America", ListFilterEnum.SOUTH_AMERICA),
    FilterChip("Australia", ListFilterEnum.AUSTRALIA),
    FilterChip("Antarctica", ListFilterEnum.ANTARCTICA)
)

/**
 * Chips row.
 * @param selectedFilter selected filter
 * @param onFilter filter callback
 */
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ChipsRow(
    selectedFilter: ListFilterEnum = ListFilterEnum.DEFAULT,
    onFilter: ((ListFilterEnum) -> Unit)? = null
) {
    val displayedChips = if (selectedFilter != ListFilterEnum.DEFAULT) {
        val selectedChip = chipsList.firstOrNull { it.type == selectedFilter }
        listOf(selectedChip) + chipsList.filterNot { it.type == selectedFilter }
    } else {
        chipsList
    }

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        displayedChips.forEach { chip ->
            chip?.let {
                SelectableChip(
                    chip = it,
                    isSelected = it.type == selectedFilter,
                    onChipClicked = { selected ->
                        onFilter?.invoke(selected)
                        GlobalScope.launch(Dispatchers.Main) {
                            if(selected != ListFilterEnum.DEFAULT) scrollState.scrollTo(0)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShowChipsRow(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        ChipsRow(
            selectedFilter = ListFilterEnum.DEFAULT
        )
    }
}

@Preview
@Composable
private fun ShowChipsRowSelected(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        ChipsRow(
            selectedFilter = ListFilterEnum.EUROPE
        )
    }
}