package fr.jhelp.tools.engine3d.resources.texture

import fr.jhelp.tools.engine3d.resources.ResourcesAccess
import fr.jhelp.tools.engine3d.scene.TextureImage

/**
 * Texture in assets file
 */
class TextureSourceAsset(private val assetPath: String) : TextureSource<TextureImage>()
{
    override fun createTexture(): TextureImage =
        ResourcesAccess.obtainTexture(this.assetPath)
}