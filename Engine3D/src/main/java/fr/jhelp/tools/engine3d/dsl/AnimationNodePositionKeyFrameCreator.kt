package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.node.AnimationNodePositionKeyFrame
import fr.jhelp.tools.engine3d.annotations.AnimationNodePositionKeyFrameDSL
import fr.jhelp.tools.engine3d.scene.Node3D

/**
 * Create an animation node position bases on key frames
 */
@AnimationNodePositionKeyFrameDSL
class AnimationNodePositionKeyFrameCreator(node: Node3D)
{
    internal val animationNodePositionKeyFrame = AnimationNodePositionKeyFrame(node)

    /**
     * Specify position for specified amount of time after animation started and interpolation used to reach it
     * @param keyTime Key time creator
     */
    @AnimationNodePositionKeyFrameDSL
    fun time(keyTime: KeyTimeCreator.() -> Unit)
    {
        val creator = KeyTimeCreator()
        creator.keyTime()
        this.animationNodePositionKeyFrame.time(creator.timeMillisecond, creator.position, creator.interpolation)
    }

    /**
     * Specify position for specified frame and interpolation used to reach it
     * @param keyFrame Key frame creator
     */
    @AnimationNodePositionKeyFrameDSL
    fun frame(keyFrame: KeyFrameCreator.() -> Unit)
    {
        val creator = KeyFrameCreator()
        creator.keyFrame()
        this.animationNodePositionKeyFrame.frame(creator.frame, creator.position, creator.interpolation)
    }
}