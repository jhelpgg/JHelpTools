package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationLoop
import fr.jhelp.tools.engine3d.animation.AnimationPause
import fr.jhelp.tools.engine3d.annotations.AnimationLoopDSL

/**
 * Create an animation loop
 */
@AnimationLoopDSL
class AnimationLoopCreator
{
    /**
     * Start animation
     */
    @AnimationLoopDSL
    var startAnimation: Animation = AnimationPause(1)

    /**
     * Loop animation
     */
    @AnimationLoopDSL
    var loopAnimation: Animation = AnimationPause(1)

    /**
     * End animation
     */
    @AnimationLoopDSL
    var endAnimation: Animation = AnimationPause(1)

    /**
     * Create animation loop
     */
    internal operator fun invoke(): AnimationLoop =
        AnimationLoop(this.startAnimation, this.loopAnimation, this.endAnimation)
}