package fr.jhelp.tools.engine3d.resources.texture

import fr.jhelp.tools.engine3d.resources.ResourcesAccess
import fr.jhelp.tools.engine3d.resources.texture
import fr.jhelp.tools.engine3d.scene.TextureImage

/**
 * Default texture
 */
data object TextureSourceDefault : TextureSource<TextureImage>()
{
    override fun createTexture(): TextureImage =
        texture(ResourcesAccess.defaultBitmap(), true)
}