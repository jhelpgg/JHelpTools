package fr.jhelp.tools.engine3d.resources.texture

import androidx.annotation.DrawableRes
import fr.jhelp.tools.engine3d.resources.ResourcesAccess
import fr.jhelp.tools.engine3d.scene.TextureImage

/**
 * Texture from drawable resources
 */
class TextureSourceDrawable(@DrawableRes private val resource: Int,
                            private val seal: Boolean = true) : TextureSource<TextureImage>()
{
    override fun createTexture(): TextureImage =
        ResourcesAccess.obtainTexture(this.resource, this.seal)
}