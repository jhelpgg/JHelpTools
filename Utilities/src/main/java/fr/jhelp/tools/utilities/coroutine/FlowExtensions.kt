package fr.jhelp.tools.utilities.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * When value match condition do action
 *
 * The action is played as soon as the condition is met. That implies if the flow's value already match, the action is immediately played.
 *
 * The action is played only one time.
 */
fun <T> Flow<T>.whenValueMatchDo(matchCondition: (T) -> Boolean, action: (T) -> Unit)
{
    var job: Job? = null

    job = CoroutineScope(Dispatchers.Default).launch {
        this@whenValueMatchDo.filter { value -> matchCondition(value) }.collect { value ->
            try
            {
                action(value)
            }
            finally
            {
                job?.cancel("Done")
            }
        }
    }
}