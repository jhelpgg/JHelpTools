package fr.jhelp.tools.viewmodel.shared

import fr.jhelp.tools.viewmodel.action.main.MainApplicationAction
import fr.jhelp.tools.viewmodel.status.MainApplicationStatus

interface MainApplicationModel : GenericModel<MainApplicationAction, MainApplicationStatus>
{
}