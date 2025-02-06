package fr.jhelp.tools.engine3d.resources.texture

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import fr.jhelp.tools.engine3d.resources.texture
import fr.jhelp.tools.engine3d.scene.TextureImage

/**
 * Texture draw by developer
 */
class TextureSourceCreated(private val width: Int,
                           private val height: Int,
                           private val draw: (Bitmap, Canvas, Paint) -> Unit) : TextureSource<TextureImage>()
{
    override fun createTexture(): TextureImage
    {
        val texture = texture(this.width, this.height)
        texture.drawOnTexture(this.draw)
        return texture
    }
}