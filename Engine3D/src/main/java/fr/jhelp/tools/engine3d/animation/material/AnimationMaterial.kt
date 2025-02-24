package fr.jhelp.tools.engine3d.animation.material

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationColor
import fr.jhelp.tools.engine3d.animation.AnimationValue
import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.dsl.animationParallel
import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Material
import fr.jhelp.tools.engine3d.scene.color3D
import fr.jhelp.tools.engine3d.scene.parts

/**
 * Animation of a material
 * @param material Material to animate
 * @param fps Animation FPS
 */
class AnimationMaterial(private val material: Material, fps: Int = 25) : Animation(fps)
{
    private val alphaAnimation: AnimationValue =
        AnimationValue(this.material.alpha, this.fps) { alpha -> this.material.alpha = alpha }
    private val diffuseAnimation : AnimationColor =
        AnimationColor(this.material.diffuse.parts, this.fps) { diffuse -> this.material.diffuse = diffuse.color3D }
    private val animationParallel = animationParallel {
        +this@AnimationMaterial.alphaAnimation
        +this@AnimationMaterial.diffuseAnimation
    }

    /**
     * Set diffuse color at given time
     * @param color Color to set
     * @param timeMilliseconds Time in milliseconds to set the color
     * @param interpolation Interpolation type to use to go to this step ([LinearInterpolation] by default)
     */
    fun diffuseAtTime(color:Color3D, timeMilliseconds:Int, interpolation:Interpolation = LinearInterpolation)
    {
        this.diffuseAnimation.time(timeMilliseconds, color.parts, interpolation)
    }

    /**
     * Set alpha at given time
     * @param alpha Alpha to set
     * @param timeMilliseconds Time in milliseconds to set the alpha
     * @param interpolation Interpolation type to use to go to this step ([LinearInterpolation] by default)
     */
    fun alphaAtTime(alpha:Float, timeMilliseconds:Int, interpolation:Interpolation = LinearInterpolation)
    {
        this.alphaAnimation.time(timeMilliseconds, alpha, interpolation)
    }

    override fun initialize()
    {
        this.animationParallel.start()
    }

    override fun animate(frame: Float): Boolean =
        this.animationParallel.animate()
}