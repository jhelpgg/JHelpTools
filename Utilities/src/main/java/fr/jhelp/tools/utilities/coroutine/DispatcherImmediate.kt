package fr.jhelp.tools.utilities.coroutine

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable

object DispatcherImmediate : CoroutineDispatcher()
{
    override fun dispatch(context : CoroutineContext, block : Runnable)
    {
        block.run()
    }
}