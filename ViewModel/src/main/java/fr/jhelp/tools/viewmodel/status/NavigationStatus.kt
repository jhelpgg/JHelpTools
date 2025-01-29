package fr.jhelp.tools.viewmodel.status

import fr.jhelp.tools.viewmodel.shared.Screen

/**
 * [fr.jhelp.tools.viewmodel.shared.NavigationModel] status
 * @param currentScreen current screen shown by the application
 */
data class NavigationStatus(val currentScreen: Screen) : GenericStatus
