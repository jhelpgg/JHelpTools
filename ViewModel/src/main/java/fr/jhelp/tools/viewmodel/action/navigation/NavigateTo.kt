package fr.jhelp.tools.viewmodel.action.navigation

import fr.jhelp.tools.viewmodel.shared.Screen

/**
 * Represents an action to navigate to a specific screen.
 */
data class NavigateTo(val screen: Screen) : NavigationAction
