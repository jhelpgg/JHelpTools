package fr.jhelp.tools.engine3d.animation.interpolation

import fr.jhelp.tools.utilities.math.compare
import fr.jhelp.tools.utilities.math.square

/**
 * Interpolation that make bounce effect
 */
object BounceInterpolation : Interpolation
{
    /**
     * Interpolate value with bounce effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float =
        when
        {
            percent.compare(0.31489f) < 0 -> 8f * square(1.1226f * percent)
            percent.compare(0.65990f) < 0 -> 8f * square(1.1226f * percent - 0.54719f) + 0.7f
            percent.compare(0.85908f) < 0 -> 8f * square(1.1226f * percent - 0.8526f) + 0.9f
            else                          -> 8f * square(1.1226f * percent - 1.0435f) + 0.95f
        }
}