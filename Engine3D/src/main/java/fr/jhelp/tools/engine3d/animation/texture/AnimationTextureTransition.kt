package fr.jhelp.tools.engine3d.animation.texture

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationList
import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.utilities.image.mixer.type.ImageMerge
import fr.jhelp.tools.utilities.image.mixer.type.ImageMixingType

/**
 * Animation texture transition. List of transitions between several texture
 * @param firstTexture First texture
 * @param secondTexture Second texture
 * @param durationMilliseconds Duration of the animation in milliseconds
 * @param imageMixingType Type of image mixing to use
 * @param interpolation Interpolation to use
 * @param animationTextureSize Size of the result texture
 */
class AnimationTextureTransition(firstTexture: TextureImage, private var secondTexture: TextureImage,
                                 durationMilliseconds: Int,
                                 private var imageMixingType: ImageMixingType = ImageMerge,
                                 interpolation: Interpolation = LinearInterpolation,
                                 private val animationTextureSize: AnimationTextureSize = AnimationTextureSize.MEDIUM) : Animation()
{
    private val animationList = AnimationList()

    init
    {
        this.animationList.add(AnimationTextureMixer(firstTexture, this.secondTexture, this.imageMixingType, durationMilliseconds, this.animationTextureSize, interpolation))
    }

    /**
     * Add a transition to a new texture
     * @param texture Texture to transition to
     * @param durationMilliseconds Duration of the animation in milliseconds
     * @param imageMixingType Type of image mixing to use
     * @param interpolation Interpolation to use
     */
    fun addTransition(texture: TextureImage, durationMilliseconds: Int, imageMixingType: ImageMixingType = this.imageMixingType, interpolation: Interpolation = LinearInterpolation)
    {
        this.animationList.add(AnimationTextureMixer(this.secondTexture, texture, imageMixingType, durationMilliseconds, this.animationTextureSize, interpolation))
        this.secondTexture = texture
    }

    override fun animate(frame: Float): Boolean =
        this.animationList.animate()
}