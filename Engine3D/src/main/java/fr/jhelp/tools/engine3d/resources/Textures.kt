package fr.jhelp.tools.engine3d.resources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import fr.jhelp.tools.engine3d.annotations.TextureSize
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.utilities.defaultBitmapBlackWhite
import fr.jhelp.tools.utilities.math.log2
import fr.jhelp.tools.utilities.source.Source
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min

fun defaultTexture(): TextureImage =
    TextureImage(defaultBitmapBlackWhite(), true)

fun texture(@TextureSize width: Int, @TextureSize height: Int): TextureImage
{
    val goodWidth = 1 shl min(9, log2(max(1, width)))
    val goodHeight = 1 shl min(9, log2(max(1, height)))
    return TextureImage(Bitmap.createBitmap(goodWidth, goodHeight, Bitmap.Config.ARGB_8888), false)
}

/**
 * Create texture from resources
 */
fun texture(resources: Resources, @DrawableRes resourceID: Int, sealed: Boolean = true): TextureImage
{
    val options = BitmapFactory.Options()
    options.inScaled = false
    return BitmapFactory.decodeResource(resources, resourceID, options)
        ?.let { bitmap -> texture(bitmap, sealed) } ?: defaultTexture()
}

/**
 * Create texture from a stream
 */
fun texture(inputStreamProvider: () -> InputStream, sealed: Boolean = true): TextureImage
{
    val options = BitmapFactory.Options()
    options.inScaled = false
    options.inJustDecodeBounds = true
    var inputStream = inputStreamProvider()
    BitmapFactory.decodeStream(inputStream, null, options)
    inputStream.close()
    val width: Int = options.outWidth
    val log2Width: Int = log2(width)
    val height: Int = options.outHeight
    val log2Height: Int = log2(height)
    val log2 = max(log2Width, log2Height)
    options.inJustDecodeBounds = false
    options.inSampleSize = 1

    if (log2 > 9)
    {
        options.inSampleSize = 1 shl (log2 - 9)
    }

    inputStream = inputStreamProvider()
    val texture = BitmapFactory.decodeStream(inputStream, null, options)
        ?.let { bitmap -> texture(bitmap, sealed) }
        ?: defaultTexture()
    inputStream.close()
    return texture
}

fun texture(source: Source, sealed: Boolean = true): TextureImage =
    try
    {
        val options = BitmapFactory.Options()
        options.inScaled = false
        options.inJustDecodeBounds = true
        var stream = source.inputStream()
        BitmapFactory.decodeStream(stream, null, options)
        stream.close()

        val width: Int = options.outWidth
        val log2Width: Int = log2(width)
        val height: Int = options.outHeight
        val log2Height: Int = log2(height)
        val log2 = max(log2Width, log2Height)
        options.inJustDecodeBounds = false
        options.inSampleSize = 1

        if (log2 > 9)
        {
            options.inSampleSize = 1 shl (log2 - 9)
        }

        stream = source.inputStream()
        val texture = BitmapFactory.decodeStream(stream, null, options)
            ?.let { bitmap -> texture(bitmap, sealed) }
            ?: defaultTexture()
        stream.close()
        texture
    }
    catch (_: Exception)
    {
        defaultTexture()
    }

fun texture(bitmap: Bitmap, sealed: Boolean = true): TextureImage
{
    val width = bitmap.width
    val height = bitmap.height
    val goodWidth = 1 shl min(9, log2(width))
    val goodHeight = 1 shl min(9, log2(height))

    return if (width != goodWidth || height != goodHeight)
    {
        val resized = Bitmap.createScaledBitmap(bitmap, goodWidth, goodHeight, false)
        bitmap.recycle()
        TextureImage(resized, sealed)
    }
    else
    {
        TextureImage(bitmap, sealed)
    }
}