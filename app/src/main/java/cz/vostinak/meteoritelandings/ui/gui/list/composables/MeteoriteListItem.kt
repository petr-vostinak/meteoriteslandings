package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cz.vostinak.meteoritelandings.BuildConfig
import cz.vostinak.meteoritelandings.R
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.tools.DateUtils
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme


/**
 * Meteorite list item.
 * @param modifier Modifier
 * @param meteoriteData Meteorite data
 */
@Composable
fun MeteoriteListItem(
    modifier: Modifier,
    meteoriteData: MeteoriteApiTO,
    onNavigate: ((MeteoriteApiTO) -> Unit)? = null
) {
    Card(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = meteoriteData.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.list_item_date, DateUtils.formatDate(meteoriteData.year)),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(R.string.list_item_reclassification, meteoriteData.recclass),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(R.string.list_item_mass, meteoriteData.mass ?: "---"),
                    style = MaterialTheme.typography.bodyMedium
                )

            }

            Column(
                modifier = Modifier.wrapContentWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    Image(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { onNavigate?.invoke(meteoriteData) },
                        painter = rememberAsyncImagePainter(generateStaticMapUrl(meteoriteData.reclat?.toDouble() ?: 0.0, meteoriteData.reclong?.toDouble() ?: 0.0)),
                        contentDescription = stringResource(R.string.list_item_image_description, meteoriteData.name)
                    )
                }
            }
        }
    }
}

/**
 * Generate static map url.
 * @param lat Latitude
 * @param lng Longitude
 * @return Static map url
 */
private fun generateStaticMapUrl(lat: Double, lng: Double): String {
    return buildString {
        append("https://maps.googleapis.com/maps/api/staticmap?center=")
        append(lat)
        append(",")
        append(lng)
        append("&zoom=2")
        append("&size=120x120")
        append("&maptype=roadmap")
        append("&markers=color:red%7C")
        append(lat)
        append(",")
        append(lng)
        append("&size=tiny")
        append("&key=")
    } + BuildConfig.MAPS_API_KEY
}

@Preview
@Composable
private fun ShowMeteoriteListItem(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        MeteoriteListItem(
            modifier = Modifier,
            meteoriteData = MeteoriteApiTO(
                name = "Aachen",
                id = 1,
                nametype = "Valid",
                recclass = "L5",
                mass = "21",
                fall = "Fell",
                year = java.util.Date(),
                reclat = 50.775000,
                reclong = 6.083330,
            )
        )
    }
}

@Preview
@Composable
private fun ShowMeteoriteListItemMassless(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        MeteoriteListItem(
            modifier = Modifier,
            meteoriteData = MeteoriteApiTO(
                name = "Aachen",
                id = 1,
                nametype = "Valid",
                recclass = "L5",
                mass = null,
                fall = "Fell",
                year = null,
                reclat = 50.775000,
                reclong = 6.083330,
            )
        )
    }
}