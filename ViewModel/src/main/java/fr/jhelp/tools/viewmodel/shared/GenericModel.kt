package fr.jhelp.tools.viewmodel.shared

import fr.jhelp.tools.viewmodel.action.GenericAction
import fr.jhelp.tools.viewmodel.status.GenericStatus
import kotlinx.coroutines.flow.StateFlow

/**
 * Generic model
 */
interface GenericModel<A:GenericAction, S:GenericStatus>
{
    /**
     * Status of model.
     */
    val status: StateFlow<S>

    /**
     * Ask model to do an action
     */
    fun action(action: A)
}