package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.FilterChip
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.ListFilterEnum
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme

@Composable
fun SelectableChip(chip: FilterChip, isSelected: Boolean, onChipClicked: ((ListFilterEnum) -> Unit)? = null) {
    Surface(
        modifier = Modifier
            .clickable {
                onChipClicked?.invoke(if(isSelected) ListFilterEnum.DEFAULT else chip.type)
            },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        contentColor = Color.White
    ) {
        Text(
            text = chip.text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun ShowChip(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        SelectableChip(
            chip = FilterChip("Europe", ListFilterEnum.EUROPE),
            isSelected = false
        )
    }
}

@Preview
@Composable
private fun ShowChipSelected(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        SelectableChip(
            chip = FilterChip("Distance", ListFilterEnum.DISTANCE),
            isSelected = true
        )
    }
}