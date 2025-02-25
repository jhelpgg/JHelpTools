package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.annotations.AnimationMaterialAlphaDSL
import kotlin.math.max

/**
 * Create an animation on material alpha
 * @property alpha Alpha to set
 */
@AnimationMaterialAlphaDSL
class AnimationMaterialAlphaCreator(@AnimationMaterialAlphaDSL var alpha: Float)
{
    /**
     * Time in milliseconds to set the alpha
     */
    @AnimationMaterialAlphaDSL
    var timeMilliseconds: Int = 1
        set(value)
        {
            field = max(1, value)
        }

    /**
     * Interpolation type to use to go to this step ([LinearInterpolation] by default)
     */
    @AnimationMaterialAlphaDSL
    var interpolation: Interpolation = LinearInterpolation
}