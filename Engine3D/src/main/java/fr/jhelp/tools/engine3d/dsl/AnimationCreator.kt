package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationPause
import fr.jhelp.tools.engine3d.annotations.AnimationCreatorDSL
import fr.jhelp.tools.engine3d.annotations.AnimationListDSL
import fr.jhelp.tools.engine3d.annotations.AnimationLoopDSL
import fr.jhelp.tools.engine3d.annotations.AnimationMaterialDSL
import fr.jhelp.tools.engine3d.annotations.AnimationNodeFollowEquationDSL
import fr.jhelp.tools.engine3d.annotations.AnimationNodePositionKeyFrameDSL
import fr.jhelp.tools.engine3d.annotations.AnimationParallelDSL
import fr.jhelp.tools.engine3d.annotations.AnimationTextureMixerDSL
import kotlin.coroutines.CoroutineContext

@AnimationCreatorDSL
class AnimationCreator
{
    internal var animation: Animation = AnimationPause(1)

    /**
     * Set animation be an animation node position key frame
     * @param node Node to animate
     * @param keyFrame Key frame creator
     */
    @AnimationCreatorDSL
    fun nodePositionKeyFrame(node: NodeReference, keyFrame: @AnimationNodePositionKeyFrameDSL AnimationNodePositionKeyFrameCreator.() -> Unit)
    {
        this.animation = animationNodePositionKeyFrame(node, keyFrame)
    }

    /**
     * Set animation be an animation node follow equation
     * @param node Node to animate
     * @param followEquation Describes equations for node position
     */
    @AnimationCreatorDSL
    fun nodeFollowEquation(node: NodeReference, followEquation: @AnimationNodeFollowEquationDSL AnimationNodeFollowEquationCreator.() -> Unit)
    {
        this.animation = animationNodeFollowEquation(node, followEquation)
    }

    /**
     * Set animation be an animation list played in sequence
     * @param animationList List of animation to play
     */
    @AnimationCreatorDSL
    fun list(animationList: @AnimationListDSL AnimationListCreator.() -> Unit)
    {
        this.animation = animationList(animationList)
    }

    /**
     * Set animation be an animation list played in parallel
     * @param animationParallel List of animation to play
     */
    @AnimationCreatorDSL
    fun parallel(animationParallel: @AnimationParallelDSL  AnimationParallelCreator.() -> Unit)
    {
        this.animation = animationParallel(animationParallel)
    }

    /**
     * Set animation be an animation loop
     * @param animationLoop Animation loop creator
     */
    @AnimationCreatorDSL
    fun loop(animationLoop: @AnimationLoopDSL AnimationLoopCreator.() -> Unit)
    {
        this.animation = animationLoop(animationLoop)
    }

    /**
     * Set animation be an animation that launch a task
     * @param coroutineContext Context to launch the task
     * @param task Task to launch
     */
    @AnimationCreatorDSL
    fun task(coroutineContext: CoroutineContext, task: () -> Unit)
    {
        this.animation = animationTask(coroutineContext, task)
    }

    /**
     * Set animation be an animation that launch a task in default context
     * @param task Task to launch
     */
    @AnimationCreatorDSL
    fun task(task: () -> Unit)
    {
        this.animation = animationTask(task)
    }

    /**
     * Set animation be an animation texture mixer
     * @param textureStart Start texture
     * @param textureEnd End texture
     * @param animationTextureMixer Animation texture mixer creator
     */
    @AnimationCreatorDSL
    fun textureMixer(textureStart: TextureReference, textureEnd: TextureReference,
                     animationTextureMixer: @AnimationTextureMixerDSL AnimationTextureMixerCreator.() -> Unit)
    {
        this.animation = animationTextureMixer(textureStart, textureEnd, animationTextureMixer)
    }

    /**
     * Set animation be an animation on material
     * @param materialReference Material to animate
     * @param animationMaterial Material animation creator
     */
    @AnimationCreatorDSL
    fun material(materialReference: MaterialReference,
                 animationMaterial: @AnimationMaterialDSL AnimationMaterialCreator.() -> Unit)
    {
        this.animation = animationMaterial(materialReference, animationMaterial)
    }
}