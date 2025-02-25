package fr.jhelp.tools.engine3d.animation.texture

import androidx.core.graphics.scale
import fr.jhelp.tools.engine3d.resources.texture
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.utilities.checkArgument
import fr.jhelp.tools.utilities.image.mixer.ImageMixer
import fr.jhelp.tools.utilities.image.mixer.type.ImageMixingType

/**
 * Texture mixer. Mix two textures to create a new texture.
 * @param firstTexture First texture
 * @param secondTexture Second texture
 * @param imageMixingType Type of image mixing to use
 */
class TextureMixer(firstTexture: TextureImage, secondTexture: TextureImage, imageMixingType: ImageMixingType, animationTextureSize: AnimationTextureSize = AnimationTextureSize.MEDIUM)
{
    /**
     * Width of the result texture
     */
    val width: Int = animationTextureSize.size

    /**
     * Height of the result texture
     */
    val height: Int = animationTextureSize.size

    /**
     * Result texture
     */
    val texture: TextureImage
    private val imageMixer: ImageMixer

    init
    {
        (firstTexture.sealed().not() && secondTexture.sealed().not()).checkArgument("Textures must not be sealed")
        val firstBitmap = firstTexture.bitmap()!!.scale(this.width, this.height)
        val secondBitmap = secondTexture.bitmap()!!.scale(this.width, this.height)
        this.imageMixer = ImageMixer(firstBitmap, secondBitmap, imageMixingType)
        this.texture = texture(this.imageMixer.resultImage, sealed = false)
    }

    var percent: Float
        get() = this.imageMixer.percent
        set(value)
        {
            this.imageMixer.percent = value
            this.texture.refresh()
        }
}