package fr.jhelp.tools.engine3d.animation.interpolation

import fr.jhelp.tools.utilities.math.EPSILON_FLOAT
import kotlin.math.max

/**
 * Interpolation with anticipation effect.
 *
 * That is to say it look goes reverse and then go to the good way, like if it take a run-up
 * @param tension Effect factor
 */
class AnticipateInterpolation(tension: Float = 1f) : Interpolation
{
    /**Effect factor*/
    private val tension = max(EPSILON_FLOAT, tension)

    /**
     * Interpolate value with anticipation effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float =
        (this.tension + 1f) * percent * percent * percent - this.tension * percent * percent
}