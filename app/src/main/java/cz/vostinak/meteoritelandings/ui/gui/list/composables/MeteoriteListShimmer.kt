package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
 * Meteorite list item with shimmer animation.
 */
@Composable
fun ShimmerItemCard() {
    Card(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .width(120.dp)
                        .height(36.dp)
                        .padding(vertical = 4.dp)
                        .brandShimmerEffect(shape = RoundedCornerShape(4.dp)),
                    text = "",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(vertical = 2.dp)
                        .brandShimmerEffect(shape = RoundedCornerShape(4.dp)),
                    text = "",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(vertical = 2.dp)
                        .brandShimmerEffect(shape = RoundedCornerShape(4.dp)),
                    text = "",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(vertical = 2.dp)
                        .brandShimmerEffect(shape = RoundedCornerShape(4.dp)),
                    text = "",
                    style = MaterialTheme.typography.bodyMedium
                )

            }

            Column(
                modifier = Modifier.wrapContentWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .brandShimmerEffect(),
                ) {  }
            }
        }
    }
}

@Preview
@Composable
private fun ShowMeteoriteListShimmer(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        ShimmerItemCard()
    }
}