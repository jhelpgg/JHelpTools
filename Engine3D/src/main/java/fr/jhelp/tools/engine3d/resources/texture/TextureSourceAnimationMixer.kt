package fr.jhelp.tools.engine3d.resources.texture

import fr.jhelp.tools.engine3d.animation.texture.AnimationTextureMixer
import fr.jhelp.tools.engine3d.scene.TextureImage

/**
 * Texture source from an animation texture mixer
 */
class TextureSourceAnimationMixer(val animationTextureMixer: AnimationTextureMixer) : TextureSource<TextureImage>()
{
    override fun createTexture(): TextureImage =
        this.animationTextureMixer.texture
}