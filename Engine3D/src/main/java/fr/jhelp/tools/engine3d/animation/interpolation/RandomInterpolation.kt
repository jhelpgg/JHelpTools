package fr.jhelp.tools.engine3d.animation.interpolation

import fr.jhelp.tools.utilities.math.nul
import fr.jhelp.tools.utilities.math.random
import fr.jhelp.tools.utilities.math.same

/**
 * Interpolation with random progression
 */
object RandomInterpolation : Interpolation
{
    /**
     * Interpolate value with random progression
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float =
        when
        {
            percent.nul || percent.same(1f) -> percent
            else                            -> random(percent, 1f)
        }
}