package fr.jhelp.tools.viewmodel.mock

import fr.jhelp.tools.viewmodel.action.main.MainApplicationAction
import fr.jhelp.tools.viewmodel.shared.MainApplicationModel
import fr.jhelp.tools.viewmodel.status.MainApplicationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainApplicationMock(override val status: StateFlow<MainApplicationStatus>,
                          private val doAction: (MainApplicationAction) -> Unit = {}) : MainApplicationModel
{
    constructor(mainApplicationStatus: MainApplicationStatus,
                doAction: (MainApplicationAction) -> Unit = {}) : this(MutableStateFlow(mainApplicationStatus), doAction)

    override fun action(action: MainApplicationAction)
    {
        this.doAction(action)
    }
}