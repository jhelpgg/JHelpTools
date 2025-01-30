package fr.jhelp.tools.viewmodel

import fr.jhelp.tools.utilities.injector.inject
import fr.jhelp.tools.viewmodel.implementation.MainApplicationImplementation
import fr.jhelp.tools.viewmodel.implementation.NavigationImplementation
import fr.jhelp.tools.viewmodel.shared.MainApplicationModel
import fr.jhelp.tools.viewmodel.shared.NavigationModel

/**
 * Inject all view models
 */
fun viewModels()
{
    inject<NavigationModel>(NavigationImplementation())
    inject<MainApplicationModel>(MainApplicationImplementation())
}