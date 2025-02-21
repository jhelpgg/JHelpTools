package fr.jhelp.tools.engine3d.animation.interpolation

/**
 * Linear interpolation
 */
object LinearInterpolation : Interpolation
{
    override fun invoke(percent: Float): Float = percent
}