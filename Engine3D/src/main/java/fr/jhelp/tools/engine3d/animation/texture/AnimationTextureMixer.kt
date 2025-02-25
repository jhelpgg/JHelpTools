package fr.jhelp.tools.engine3d.animation.texture

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.utilities.image.mixer.type.ImageMixingType
import kotlin.math.max

/**
 * Animation texture mixer. Mix two textures to create a new texture.
 * @param firstTexture First texture
 * @param secondTexture Second texture
 * @param imageMixingType Type of image mixing to use
 * @param durationMilliseconds Duration of the animation in milliseconds
 * @param animationTextureSize Size of the result texture
 * @param interpolation Interpolation to use
 * @param fps Animation frame per seconds
 */
class AnimationTextureMixer(firstTexture: TextureImage, secondTexture: TextureImage, imageMixingType: ImageMixingType,
                            durationMilliseconds: Int, animationTextureSize: AnimationTextureSize = AnimationTextureSize.MEDIUM,
                            private val interpolation: Interpolation = LinearInterpolation,
                            fps: Int = 25) : Animation(fps)
{
    private val numberFrame = max(1f, (durationMilliseconds * this.fps) / 1000f)
    private val textureMixer = TextureMixer(firstTexture, secondTexture, imageMixingType, animationTextureSize)
    val texture: TextureImage get() = this.textureMixer.texture

    override fun animate(frame: Float): Boolean
    {
        this@AnimationTextureMixer.textureMixer.percent = this@AnimationTextureMixer.interpolation(frame / this@AnimationTextureMixer.numberFrame)
        return frame <= this.numberFrame
    }
}