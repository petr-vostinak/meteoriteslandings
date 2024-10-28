package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cz.vostinak.meteoritelandings.R
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.ListFilterEnum
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.MainListState
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme
import java.util.Date


/**
 * Main list screen.
 * @param modifier Modifier
 * @param screenState MainListState
 */
@Composable
fun MainListContent(
    modifier: Modifier,
    screenState: MainListState? = null,
    onSyncRetry: () -> Unit = {},
    onFilter: ((ListFilterEnum) -> Unit)? = null,
    onNavigate: ((MeteoriteApiTO) -> Unit)? = null
) {
    // Loading shimmer
    AnimatedVisibility(screenState == null) {
        Column(
            modifier = modifier
        ) {
            ShimmerChipsRow()

            LazyColumn {
                item {

                }

                items(count = 10) {
                    ShimmerItemCard()
                }
            }
        }
    }

    // Error content
    AnimatedVisibility(screenState != null && !screenState.errorMessage.isNullOrEmpty()) {
        EmptyCard(
            modifier = modifier,
            subtitle = screenState?.errorMessage ?: "",
            button1Text = stringResource(R.string.action_retry),
            button1Action = onSyncRetry
        )
    }

    // Main list content
    AnimatedVisibility(screenState != null && screenState.errorMessage.isNullOrEmpty()) {
        Column(
            modifier = modifier
        ) {
            ChipsRow(
                selectedFilter = screenState?.filter ?: ListFilterEnum.DEFAULT,
                onFilter = onFilter
            )

            LazyColumn {
                items(
                    count = screenState?.meteoritesList?.size ?: 0,
                    key = { screenState?.meteoritesList?.getOrNull(it)?.id ?: 0 },
                ) { index ->
                    screenState?.meteoritesList?.getOrNull(index)?.let { meteoriteData ->
                        MeteoriteListItem(
                            modifier = Modifier,
                            meteoriteData = meteoriteData,
                            onNavigate = onNavigate
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
private fun ShowMAinListContent(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        MainListContent(
            modifier = Modifier,
            screenState = MainListState(
                meteoritesList = listOf(
                    MeteoriteApiTO(
                        name = "Aachen",
                        id = 1,
                        nametype = "Valid",
                        recclass = "L5",
                        mass = "21",
                        fall = "Fell",
                        year = Date(),
                        reclat = 50.775000,
                        reclong = 6.083330,
                    ),
                    MeteoriteApiTO(
                        name = "Aarhus",
                        id = 2,
                        nametype = "Valid",
                        recclass = "H6",
                        mass = "720",
                        fall = "Fell",
                        year = Date(),
                        reclat = 56.18333,
                        reclong = 10.23333,
                    ),
                    MeteoriteApiTO(
                        name = "Abee",
                        id = 6,
                        nametype = "Valid",
                        recclass = "EH4",
                        mass = "107000",
                        fall = "Fell",
                        year = Date(),
                        reclat = 54.216670,
                        reclong = -113.000000,
                    )
                ),
                errorMessage = null
            )
        )
    }
}

@Preview
@Composable
private fun ShowMainListContentError(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        MainListContent(
            modifier = Modifier,
            screenState = MainListState(
                meteoritesList = listOf(),
                errorMessage = "Tady je chyba a jeji text..."
            )
        )
    }
}

@Preview
@Composable
private fun ShowMainListLoading(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        MainListContent(
            modifier = Modifier,
            screenState = null
        )
    }
}