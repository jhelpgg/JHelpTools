package fr.jhelp.tools

import fr.jhelp.tools.ui.ApplicationJHelp
import fr.jhelp.tools.viewmodel.viewModels

class MainApplication : ApplicationJHelp()
{
    override fun onCreateInternal()
    {
        viewModels()
    }
}