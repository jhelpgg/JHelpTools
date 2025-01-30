package fr.jhelp.tools.viewmodel.mock

import fr.jhelp.tools.viewmodel.action.navigation.NavigationAction
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.status.NavigationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigation mock can be used in tests.
 */
class NavigationMock(override val status: StateFlow<NavigationStatus>,
                     private val doAction: (NavigationAction) -> Unit = {}) : NavigationModel
{
    constructor(navigationStatus: NavigationStatus,
                doAction: (NavigationAction) -> Unit = {}) : this(MutableStateFlow(navigationStatus), doAction)

    override fun action(action: NavigationAction)
    {
        this.doAction(action)
    }
}