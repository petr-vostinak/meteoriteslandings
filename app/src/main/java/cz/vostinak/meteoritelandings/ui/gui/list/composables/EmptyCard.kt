package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import cz.vostinak.meteoritelandings.R
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme


/**
 * Empty card for errors and empty list.
 * @param modifier Modifier
 * @param title Title
 * @param subtitle Subtitle
 * @param button1Text Button 1 text
 * @param button1Action Button 1 action
 * @param button2Text Button 2 text
 * @param button2Action Button 2 action
 */
@Composable
fun EmptyCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    button1Text: String? = null,
    button1Action: (() -> Unit)? = null,
    button2Text: String? = null,
    button2Action: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image
            Image(
                painter = painterResource(id = R.drawable.ic_empty),
                contentDescription = stringResource(id = R.string.empty_card_image_desc),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 20.dp)
                    .size(200.dp, 200.dp)
            )

            // Title
            if (!title.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 0.dp, end = 8.dp, bottom = 5.dp),
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Subtitle
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    text = subtitle,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(24.dp))

            // Buttons if any
            if (button1Text.isNullOrEmpty() && button2Text.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
            } else {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp))
                {
                    if(!button1Text.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(enabled = button1Action != null) {
                                    button1Action?.invoke()
                                },
                            text = button1Text.uppercase(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if(!button2Text.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(enabled = button2Action != null) {
                                    button2Action?.invoke()
                                },
                            text = button2Text.uppercase(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShowEmptyCardOneButton(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        EmptyCard(
            modifier = Modifier,
            title = "No meteorites found",
            subtitle = "Please try again later",
            button1Text = "Retry",
            button1Action = {},
            button2Text = null,
            button2Action = null
        )
    }
}

@Preview
@Composable
private fun ShowEmptyCardTwoButtons(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        EmptyCard(
            modifier = Modifier,
            title = "No meteorites found",
            subtitle = "Please try again later",
            button1Text = "Retry",
            button1Action = {},
            button2Text = "Cancel",
            button2Action = {}
        )
    }
}