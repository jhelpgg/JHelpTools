package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationParallel
import fr.jhelp.tools.engine3d.annotations.AnimationParallelDSL

/**
 * Create an animation list played in parallel
 */
@AnimationParallelDSL
class AnimationParallelCreator
{
    internal val animationParallel = AnimationParallel()

    /**
     * Add animation to the list
     */
    @AnimationParallelDSL
    operator fun Animation.unaryPlus()
    {
        this@AnimationParallelCreator.animationParallel.add(this)
    }
}