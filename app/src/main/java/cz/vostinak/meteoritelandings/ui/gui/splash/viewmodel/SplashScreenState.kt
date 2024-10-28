package cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel


/**
 * Splash screen state.
 */
data class SplashScreenState(
    /** Is synchronization done */
    val syncDone: Boolean = false,
    /** Synchronization error */
    val error: SyncStateEnum = SyncStateEnum.SYNC_IN_PROGRESS
)
