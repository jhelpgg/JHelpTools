package fr.jhelp.tools.engine3d.animation

import android.os.SystemClock
import fr.jhelp.tools.utilities.math.bounds

/**
 * A generic animation
 */
abstract class Animation(fps: Int = 25)
{
    /** Animation frame per seconds */
    val fps: Int = fps.bounds(1, 100)
    private var statTime = 0L

    /**
     * Start the animation
     */
    internal fun start()
    {
        this.statTime = SystemClock.uptimeMillis()
        this.initialize()
        this.animate(0f)
    }

    /**
     * Call to refresh animation
     *
     * @return `true`if animation continue. `false` if animation finished
     */
    internal fun animate(): Boolean =
        this.animate(((SystemClock.uptimeMillis() - this.statTime) * this.fps) / 1000f)

    /**
     * Transform time to number of frame, depends on animation FPS
     */
    fun millisecondsToFrame(milliseconds: Int): Int = (milliseconds * this.fps) / 1000

    /**
     * Called when animation finished
     *
     * Does nothing by default
     */
    internal open fun finished(): Unit = Unit

    /**
     * Called when animation about to start.
     *
     * Does nothing by default
     */
    protected open fun initialize(): Unit = Unit

    /**
     * Play animation for given frame.
     *
     * @return `true` if the animation continue. `false` if animation is finished
     */
    protected abstract fun animate(frame: Float): Boolean
}