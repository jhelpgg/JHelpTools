package fr.jhelp.tools.viewmodel.implementation

import fr.jhelp.tools.viewmodel.action.main.MainApplicationAction
import fr.jhelp.tools.viewmodel.shared.MainApplicationModel
import fr.jhelp.tools.viewmodel.status.MainApplicationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class MainApplicationImplementation : MainApplicationModel
{
    private val statusMutable = MutableStateFlow<MainApplicationStatus>(MainApplicationStatus.IDLE)
    override val status: StateFlow<MainApplicationStatus> = this.statusMutable.asStateFlow()

    override fun action(action: MainApplicationAction)
    {
        when(action)
        {
            MainApplicationAction.PAUSE -> this.statusMutable.value = MainApplicationStatus.PAUSE
            MainApplicationAction.RESUME -> this.statusMutable.value = MainApplicationStatus.ACTIVE
            MainApplicationAction.EXIT -> this.statusMutable.value = MainApplicationStatus.EXIT
        }
    }
}