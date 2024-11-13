package cz.vostinak.meteoritelandings.ui.gui.splash.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import cz.vostinak.meteoritelandings.R
import cz.vostinak.meteoritelandings.ui.gui.preview.Theme
import cz.vostinak.meteoritelandings.ui.gui.preview.ThemePreviewProvider
import cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel.SplashScreenState
import cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel.SyncStateEnum
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme


/**
 * Splash screen.
 * @param state screen data
 * @param onRetry listener for API synchronization
 * @param onFinish listener for exit the app
 * @param onOffline listener for offline mode
 */
@Composable
fun SplashScreen(
    state: SplashScreenState? = null,
    onRetry: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    onOffline: (() -> Unit)? = null
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .aspectRatio(1f)
                        .padding(32.dp),
                    painter = painterResource(R.drawable.ic_meteorite),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }

            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.splash_synchronizing),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(state?.error == SyncStateEnum.ERROR_NO_LOCAL_DATA) {
            Dialog(
                dialogTitle = stringResource(R.string.splash_error_title),
                dialogText = stringResource(R.string.splash_error_message_no_db),
                dialogConfirmButtonText = stringResource(R.string.action_retry),
                dialogDismissButtonText = stringResource(R.string.action_cancel),
                onDismissRequest = {
                    onFinish?.invoke()
                },
                onConfirmation = {
                    onRetry?.invoke()
                }
            )
        }

        AnimatedVisibility(state?.error == SyncStateEnum.ERROR_LOCAL_DATA){
            Dialog(
                dialogTitle = stringResource(R.string.splash_error_title),
                dialogText = stringResource(R.string.splash_error_message_with_db),
                dialogConfirmButtonText = stringResource(R.string.action_retry),
                dialogDismissButtonText = stringResource(R.string.action_continue_offline),
                onDismissRequest = {
                    onOffline?.invoke()
                },
                onConfirmation = {
                    onRetry?.invoke()
                }
            )
        }
    }
}

@Composable
fun Dialog(
    dialogTitle: String,
    dialogText: String,
    dialogConfirmButtonText: String,
    dialogDismissButtonText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = dialogConfirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = dialogDismissButtonText)
            }
        }
    )
}

@Preview
@Composable
private fun ShowSplashScreen(@PreviewParameter(ThemePreviewProvider::class) theme: Theme) {
    MeteoriteLandingsTheme(
        darkTheme = theme.isDarkMode
    ) {
        SplashScreen()
    }
}