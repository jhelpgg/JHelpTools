package fr.jhelp.tools.engine3d.animation.interpolation

import fr.jhelp.tools.utilities.math.EPSILON_FLOAT
import kotlin.math.max
import kotlin.math.pow

/**
 * Interpolation with acceleration effect
 * @param factor Acceleration factor
 */
class AccelerationInterpolation(factor: Float = 1f) : Interpolation
{
    /**Acceleration factor*/
    private val factor = 2.0 * max(EPSILON_FLOAT.toDouble(), factor.toDouble())

    /**
     * Interpolate value with acceleration effect
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float) : Float = percent.toDouble().pow(this.factor).toFloat()
}