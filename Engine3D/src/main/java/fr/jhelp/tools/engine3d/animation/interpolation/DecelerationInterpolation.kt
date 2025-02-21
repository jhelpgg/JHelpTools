package fr.jhelp.tools.engine3d.animation.interpolation

import fr.jhelp.tools.utilities.math.EPSILON_FLOAT
import kotlin.math.max
import kotlin.math.pow

/**
 * Interpolation with deceleration effect
 * @param factor Deceleration factor
 */
class DecelerationInterpolation(factor: Float = 1f) : Interpolation
{
    /**Deceleration factor*/
    private val factor = 2.0 * max(EPSILON_FLOAT.toDouble(), factor.toDouble())

    /**
     * Interpolate value with deceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float =
        (1.0 - (1.0 - percent).pow(this.factor)).toFloat()
}
