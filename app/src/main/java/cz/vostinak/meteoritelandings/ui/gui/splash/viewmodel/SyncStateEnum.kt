package cz.vostinak.meteoritelandings.ui.gui.splash.viewmodel

/**
 * Enum for synchronization result state.
 */
enum class SyncStateEnum {
    /**
     * Synchronization in progress.
     */
    SYNC_IN_PROGRESS,
    /**
     * Synchronization done successfully.
     */
    SYNC_DONE,
    /**
     * Error when no local data is available.
     */
    ERROR_NO_LOCAL_DATA,
    /**
     * Error when local data is available.
     */
    ERROR_LOCAL_DATA
}