package fr.jhelp.tools.ui

import android.app.Application
import android.content.Context
import android.util.Log
import fr.jhelp.tools.utilities.injector.inject

open class ApplicationJHelp : Application()
{
    final override fun onCreate()
    {
        super.onCreate()
        val applicationContext = this.applicationContext
        inject<Context>(applicationContext)
        Log.d("ApplicationJHelp", "onCreate")
        this.onCreateInternal()
    }

    protected open fun onCreateInternal()
    {
    }
}