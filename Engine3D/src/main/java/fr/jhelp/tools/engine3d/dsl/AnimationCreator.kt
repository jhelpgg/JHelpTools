package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationPause
import fr.jhelp.tools.engine3d.annotations.AnimationCreator
import fr.jhelp.tools.engine3d.annotations.AnimationListDSL
import fr.jhelp.tools.engine3d.annotations.AnimationLoopDSL
import fr.jhelp.tools.engine3d.annotations.AnimationNodeFollowEquationDSL
import fr.jhelp.tools.engine3d.annotations.AnimationNodePositionKeyFrameDSL
import fr.jhelp.tools.engine3d.annotations.AnimationParallelDSL
import kotlin.coroutines.CoroutineContext

@AnimationCreator
class AnimationCreator
{
    internal var animation : Animation = AnimationPause(1)

    /**
     * Create an animation node position key frame
     * @param node Node to animate
     * @param keyFrame Key frame creator
     * @return Created animation
     */
    @AnimationNodePositionKeyFrameDSL
    fun nodePositionKeyFrame(node: NodeReference, keyFrame: AnimationNodePositionKeyFrameCreator.() -> Unit)
    {
        this.animation = animationNodePositionKeyFrame(node, keyFrame)
    }

    /**
     * Create an animation node follow equation
     * @param node Node to animate
     * @param followEquation Describes equations for node position
     * @return Created animation
     */
    @AnimationNodeFollowEquationDSL
    fun nodeFollowEquation(node: NodeReference, followEquation: AnimationNodeFollowEquationCreator.() -> Unit)
    {
        this.animation = animationNodeFollowEquation(node, followEquation)
    }

    /**
     * Create an animation list played in sequence
     * @param animationList List of animation to play
     * @return Created animation
     */
    @AnimationListDSL
    fun list(animationList: AnimationListCreator.() -> Unit)
    {
        this.animation = animationList(animationList)
    }

    /**
     * Create an animation list played in parallel
     * @param animationParallel List of animation to play
     * @return Created animation
     */
    @AnimationParallelDSL
    fun parallel(animationParallel: AnimationParallelCreator.() -> Unit)
    {
        this.animation = animationParallel(animationParallel)
    }
    /**
     * Create an animation loop
     * @param animationLoop Animation loop creator
     * @return Created animation
     */
    @AnimationLoopDSL
    fun loop(animationLoop: AnimationLoopCreator.() -> Unit)
    {
        this.animation = animationLoop(animationLoop)
    }

    /**
     * Create an animation that launch a task
     * @param coroutineContext Context to launch the task
     * @param task Task to launch
     * @return Created animation
     */
    @AnimationCreator
    fun task(coroutineContext: CoroutineContext, task: () -> Unit)
    {
        this.animation = animationTask(coroutineContext, task)
    }
    /**
     * Create an animation that launch a task in default context
     * @param task Task to launch
     * @return Created animation
     */
    @AnimationCreator
    fun task(task: () -> Unit)
    {
        this.animation = animationTask(task)
    }
}