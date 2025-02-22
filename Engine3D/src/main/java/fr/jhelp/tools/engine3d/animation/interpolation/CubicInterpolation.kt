package fr.jhelp.tools.engine3d.animation.interpolation

import fr.jhelp.tools.utilities.math.cubic

/**
 * Cubic interpolation
 * @param firstControl First control point
 * @param secondControl Second control point
 */
class CubicInterpolation(private val firstControl: Float = 0.1f,
                         private val secondControl: Float = 0.9f) : Interpolation
{
    /**
     * Compute cubic interpolation
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float =
        cubic(0f,
              this.firstControl,
              this.secondControl,
              1f,
              percent)
}