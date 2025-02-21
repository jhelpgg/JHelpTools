package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationLaunchTask
import fr.jhelp.tools.engine3d.annotations.AnimationDSL
import kotlin.coroutines.CoroutineContext

/**
 * Create an animation node position key frame
 * @param node Node to animate
 * @param keyFrame Key frame creator
 * @return Created animation
 */
@AnimationDSL
fun animationNodePositionKeyFrame(node: NodeReference, keyFrame: AnimationNodePositionKeyFrameCreator.() -> Unit) : Animation
{
    val creator = AnimationNodePositionKeyFrameCreator(node.node)
    creator.keyFrame()
    return creator.animationNodePositionKeyFrame
}

/**
 * Create an animation node follow equation
 * @param node Node to animate
 * @param followEquation Describes equations for node position
 * @return Created animation
 */
@AnimationDSL
fun animationNodeFollowEquation(node: NodeReference, followEquation: AnimationNodeFollowEquationCreator.() -> Unit) : Animation
{
    val creator = AnimationNodeFollowEquationCreator(node)
    creator.followEquation()
    return creator()
}

/**
 * Create an animation list played in sequence
 * @param animationList List of animation to play
 * @return Created animation
 */
@AnimationDSL
fun animationList(animationList: AnimationListCreator.() -> Unit) : Animation
{
    val creator = AnimationListCreator()
    creator.animationList()
    return creator.animationList
}

/**
 * Create an animation list played in parallel
 * @param animationParallel List of animation to play
 * @return Created animation
 */
@AnimationDSL
fun animationParallel(animationParallel: AnimationParallelCreator.() -> Unit) : Animation
{
    val creator = AnimationParallelCreator()
    creator.animationParallel()
    return creator.animationParallel
}

/**
 * Create an animation loop
 * @param animationLoop Animation loop creator
 * @return Created animation
 */
@AnimationDSL
fun animationLoop(animationLoop: AnimationLoopCreator.() -> Unit) : Animation
{
    val creator = AnimationLoopCreator()
    creator.animationLoop()
    return creator()
}

/**
 * Create an animation that launch a task
 * @param coroutineContext Context to launch the task
 * @param task Task to launch
 * @return Created animation
 */
@AnimationDSL
fun animationTask(coroutineContext: CoroutineContext, task: () -> Unit) : Animation =
    AnimationLaunchTask(coroutineContext, task)

/**
 * Create an animation that launch a task in default context
 * @param task Task to launch
 * @return Created animation
 */
@AnimationDSL
fun animationTask(task: () -> Unit) : Animation =
    AnimationLaunchTask(task)
