package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import cz.vostinak.meteoritelandings.tools.brandShimmerEffect
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme


/**
 * Shimmer effect for chips row.
 */
@Composable
fun ShimmerChipsRow() {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            modifier = Modifier
                .brandShimmerEffect(shape = RoundedCornerShape(16.dp))
                .width(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = " ",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .brandShimmerEffect(shape = RoundedCornerShape(16.dp))
                .width(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = " ",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .brandShimmerEffect(shape = RoundedCornerShape(16.dp))
                .width(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = " ",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .brandShimmerEffect(shape = RoundedCornerShape(16.dp))
                .width(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = " ",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .brandShimmerEffect(shape = RoundedCornerShape(16.dp))
                .width(100.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = " ",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun ShowShimmerChipsRow(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        ShimmerChipsRow()
    }
}