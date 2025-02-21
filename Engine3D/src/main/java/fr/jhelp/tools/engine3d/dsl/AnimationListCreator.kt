package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationList
import fr.jhelp.tools.engine3d.animation.AnimationPause
import fr.jhelp.tools.engine3d.annotations.AnimationListDSL

/**
 * Create an animation list played in sequence
 */
@AnimationListDSL
class AnimationListCreator
{
    internal val animationList = AnimationList()

    /**
     * Add animation to the list
     */
    @AnimationListDSL
    operator fun Animation.unaryPlus()
    {
        this@AnimationListCreator.animationList.add(this)
    }

    /**
     * Add pause to the list
     */
    @AnimationListDSL
    fun pause(durationMilliseconds: Int)
    {
        this.animationList.add(AnimationPause(durationMilliseconds))
    }
}