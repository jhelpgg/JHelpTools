package fr.jhelp.tools.engine3d.animation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

/**
 * Animation to launch a task
 * @property coroutineContext Context to launch the task
 * @property task Task to launch
 */
class AnimationLaunchTask(private val coroutineContext: CoroutineContext,
                          private val task:()->Unit) : Animation()
{
    constructor(task:()->Unit) : this(Dispatchers.Default, task)

    private val played = AtomicBoolean(false)

    override fun initialize()
    {
        this.played.set(false)
    }

    override fun animate(frame: Float): Boolean
    {
        if(this.played.compareAndSet(false, true))
        {
           CoroutineScope(this.coroutineContext).launch { this@AnimationLaunchTask.task() }
        }

        return false
    }
}