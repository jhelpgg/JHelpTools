package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.animation.texture.AnimationTextureMixer
import fr.jhelp.tools.engine3d.animation.texture.AnimationTextureSize
import fr.jhelp.tools.engine3d.annotations.AnimationTextureMixerDSL
import fr.jhelp.tools.engine3d.resources.defaultTexture
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.utilities.image.mixer.type.ImageMerge
import fr.jhelp.tools.utilities.image.mixer.type.ImageMixingType
import kotlin.math.max

/**
 * Create an animation texture mixer
 * @param startTexture Start texture
 * @param endTexture End texture
 */
@AnimationTextureMixerDSL
class AnimationTextureMixerCreator(val startTexture: TextureReference, val endTexture: TextureReference)
{
    /**
     * Duration of the animation in milliseconds
     */
    @AnimationTextureMixerDSL
    var durationMilliseconds: Int = 1
        set(value)
        {
            field = max(1, value)
        }

    /**
     * Type of image mixing to use
     */
    @AnimationTextureMixerDSL
    var imageMixingType: ImageMixingType = ImageMerge

    /**
     * Interpolation to use
     */
    @AnimationTextureMixerDSL
    var interpolation: Interpolation = LinearInterpolation

    /**
     * Size of the result texture
     */
    @AnimationTextureMixerDSL
    var animationTextureSize: AnimationTextureSize = AnimationTextureSize.MEDIUM

    internal operator fun invoke(): AnimationTextureMixer =
        AnimationTextureMixer(this.startTexture.textureSource.texture as? TextureImage ?: defaultTexture(false),
                              this.endTexture.textureSource.texture as? TextureImage ?: defaultTexture(false),
                              this.imageMixingType, this.durationMilliseconds, this.animationTextureSize, this.interpolation)
}