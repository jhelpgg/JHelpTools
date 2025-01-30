package fr.jhelp.tools.utilities.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

val Dispatchers.Immediate : CoroutineDispatcher get() = DispatcherImmediate
