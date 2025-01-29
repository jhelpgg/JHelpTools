package fr.jhelp.tools.viewmodel.shared

import fr.jhelp.tools.viewmodel.action.navigation.NavigationAction
import fr.jhelp.tools.viewmodel.status.NavigationStatus

/**
 * Navigation model
 */
interface NavigationModel : GenericModel<NavigationAction, NavigationStatus>
