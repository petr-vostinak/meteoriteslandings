package cz.vostinak.meteoritelandings.ui.gui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.vostinak.meteoritelandings.api.nasa.to.MeteoriteApiTO
import cz.vostinak.meteoritelandings.ui.gui.list.composables.MainListScreen
import cz.vostinak.meteoritelandings.ui.gui.list.veiwmodel.MainViewModel
import cz.vostinak.meteoritelandings.ui.gui.map.MapScreen
import cz.vostinak.meteoritelandings.ui.gui.splash.composables.SplashScreen
import cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel.SplashViewModel
import cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel.SyncStateEnum
import cz.vostinak.meteoritelandings.ui.theme.MeteoriteLandingsTheme


/**
 * Main navigation host.
 * @param onFinish Callback for exit the app
 */
@Composable
fun MainNavHost(
    mainViewModel: MainViewModel,
    onFinish: (() -> Unit)? = null,
    onNavigate: ((MeteoriteApiTO) -> Unit)? = null
) {
    val navController: NavHostController = rememberNavController()

    MeteoriteLandingsTheme {
        NavHost(
            navController = navController,
            startDestination = NavigationDestinations.SPLASH
        ) {
            // Splash screen
            composable(route = NavigationDestinations.SPLASH) {
                val viewModel = hiltViewModel<SplashViewModel>()
                val state = viewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.getMeteoritesList()
                }

                SplashScreen(
                    state = state.value,
                    onRetry = {
                        viewModel.getMeteoritesList()
                    },
                    onOffline = {
                        navController.navigate(NavigationDestinations.LIST) {
                            popUpTo(NavigationDestinations.SPLASH) {
                                inclusive = true
                            }
                        }
                    },
                    onFinish = {
                        onFinish?.invoke()
                    }
                )

                if(state.value.syncDone && state.value.error == SyncStateEnum.SYNC_DONE) {
                    navController.navigate(NavigationDestinations.LIST)
                }
            }


            // Main list screen
            composable(route = NavigationDestinations.LIST) {
                val screenState = mainViewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    mainViewModel.getMeteoritesList()
                }

                MainListScreen(
                    meteoritesListState = screenState.value,
                    onMap = {
                        navController.navigate(NavigationDestinations.MAP)
                    },
                    onRetrySync = {
                        mainViewModel.resetSyncTime()
                        navController.navigate(NavigationDestinations.SPLASH)
                    },
                    onFilter = { filter ->
                        mainViewModel.filterList(filter)
                    },
                    onBack = {
                        onFinish?.invoke()
                    },
                    onNavigate = { meteorite ->
                        onNavigate?.invoke(meteorite)
                    }
                )
            }

            // Map screen
            composable(route = NavigationDestinations.MAP) {
                MapScreen(
                    viewModel = mainViewModel,
                    openMapsWithCoordinates = {

                    },
                    onNavigateToRoute = {

                    },
                    getLastLocation = {
                        mainViewModel.updateLastLocationState()
                    }
                )
            }
        }
    }
}