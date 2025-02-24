package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.annotations.AnimationMaterialDSL
import fr.jhelp.tools.engine3d.scene.Color3D
import kotlin.math.max

/**
 * Create an animation on material diffuse
 * @property color3D Color to set
 */
@AnimationMaterialDSL
class AnimationMaterialDiffuseCreator(var color3D: Color3D)
{
    /**
     * Time in milliseconds to set the color
     */
    @AnimationMaterialDSL
    var timeMilliseconds: Int = 1
        set(value)
        {
            field = max(1, value)
        }

    /**
     * Interpolation type to use to go to this step ([LinearInterpolation] by default)
     */
    @AnimationMaterialDSL
    var interpolation: Interpolation = LinearInterpolation
}