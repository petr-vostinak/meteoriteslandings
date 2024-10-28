package cz.vostinak.meteoritelandings.ui.gui.list.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cz.vostinak.meteoritelandings.R
import cz.vostinak.meteoritelandings.api.common.Resource
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.EmptyDbThrowable
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.EmptyFilterThrowable
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.ListFilterEnum
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.LocationNotAvailableThrowable
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.MainListState


/**
 * Main list screen.
 * @param meteoritesListState screen data
 * @param onMap listener for map screen
 * @param onRetrySync listener for API synchronization
 * @param onFilter listener for filter
 * @param onBack listener for return action
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainListScreen(
    meteoritesListState: Resource<MainListState>,
    onMap: (() -> Unit)? = null,
    onRetrySync: (() -> Unit)? = null,
    onFilter: ((ListFilterEnum) -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    onNavigate: ((MeteoriteApiTO) -> Unit)? = null
) {
    BackHandler {
        onBack?.invoke()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(
                        onClick = {
                            onMap?.invoke()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_map),
                            contentDescription = "Map"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (meteoritesListState) {
            is Resource.Success -> {
                MainListContent(
                    modifier = Modifier.padding(innerPadding),
                    screenState = meteoritesListState.data,
                    onSyncRetry = { onRetrySync?.invoke() },
                    onFilter = { onFilter?.invoke(it) },
                    onNavigate = { onNavigate?.invoke(it) }
                )
            }

            is Resource.Error -> {
                when(meteoritesListState.exception?.cause) {
                    is LocationNotAvailableThrowable -> {
                        EmptyCard(
                            modifier = Modifier.padding(innerPadding),
                            title = stringResource(R.string.error),
                            subtitle = meteoritesListState.exception.message,
                            button1Text = stringResource(R.string.action_clear_filter),
                            button1Action = { onFilter?.invoke(ListFilterEnum.DEFAULT) }
                        )
                    }
                    is EmptyDbThrowable -> {
                        EmptyCard(
                            modifier = Modifier.padding(innerPadding),
                            title = stringResource(R.string.error),
                            subtitle = meteoritesListState.exception.message,
                            button1Text = stringResource(R.string.action_retry),
                            button1Action = { onRetrySync?.invoke() }
                        )

                    }
                    is EmptyFilterThrowable -> {
                        EmptyCard(
                            modifier = Modifier.padding(innerPadding),
                            title = stringResource(R.string.error),
                            subtitle = meteoritesListState.exception.message,
                            button1Text = stringResource(R.string.action_clear_filter),
                            button1Action = { onFilter?.invoke(ListFilterEnum.DEFAULT) }
                        )
                    }

                    else -> {
                        EmptyCard(
                            modifier = Modifier.padding(innerPadding),
                            title = stringResource(R.string.error),
                            subtitle = stringResource(R.string.unknown_error)
                        )
                    }
                }
            }

            else -> {
                MainListContent(
                    modifier = Modifier.padding(innerPadding),
                    screenState = null
                )
            }
        }
    }
}