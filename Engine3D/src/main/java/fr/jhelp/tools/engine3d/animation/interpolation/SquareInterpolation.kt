package fr.jhelp.tools.engine3d.animation.interpolation

/**
 * Interpolation follow square progression
 */
object SquareInterpolation : Interpolation
{
    /**
     * Interpolate value with following equation:
     *
     *    t²
     *
     * @param percent Value to interpolate
     * @return Interpolate value
     */
    override operator fun invoke(percent: Float): Float = percent * percent
}
