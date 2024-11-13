package cz.vostinak.meteoritelandings.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Extension function for combine with StateFlow
 * @param flow StateFlow flow to combine with
 * @param scope CoroutineScope
 * @param sharingStarted SharingStarted
 * @param transform Function to transform the values
 */
fun <T1, T2, R> StateFlow<T1>.combineState(
    flow: StateFlow<T2>,
    scope: CoroutineScope = GlobalScope,
    sharingStarted: SharingStarted = SharingStarted.Eagerly,
    transform: (T1, T2) -> R
) : StateFlow<R> = combine(this, flow) { o1, o2 ->
    transform.invoke(o1, o2)
}.stateIn(scope, sharingStarted, transform.invoke(this.value, flow.value))