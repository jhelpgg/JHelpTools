package fr.jhelp.tools.engine3d.resources

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.DrawableRes
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.utilities.injector.isInjected

/**
 * Access to resources helper
 *
 * It suppose that application context is provided
 */
object ResourcesAccess
{
    private val applicationContext: Context by injected<Context>()
    private val resources: Resources by lazy { this.applicationContext.resources }
    private val assetManager: AssetManager by lazy { this.applicationContext.assets }

    /**
     * Create default Bitmap
     */
    fun defaultBitmap(): Bitmap
    {
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(4)
        pixels[0] = 0xFFFFFFFF.toInt()
        pixels[1] = 0xFF000000.toInt()
        pixels[2] = 0xFF000000.toInt()
        pixels[3] = 0xFFFFFFFF.toInt()
        bitmap.setPixels(pixels, 0, 2, 0, 0, 2, 2)
        return bitmap
    }

    /**
     * Obtain texture from drawable resources
     */
    fun obtainTexture(@DrawableRes drawableID: Int, sealed: Boolean = true): TextureImage =
        if (isInjected(Context::class.java))
        {
            texture(this.resources, drawableID, sealed)
        }
        else
        {
            defaultTexture(sealed)
        }

    /**
     * Obtain texture from assets
     */
    fun obtainTexture(assetPath: String, sealed: Boolean = true): TextureImage =
        if (isInjected(Context::class.java))
        {
            try
            {
                texture({ this.assetManager.open(assetPath) }, sealed)
            }
            catch (exception: Exception)
            {
                Log.w("ResourcesAccess", "Issue while loading asset : $assetPath", exception)
                defaultTexture(sealed)
            }
        }
        else
        {
            defaultTexture(sealed)
        }

    /**
     * Obtain bitmap from drawable resources
     */
    fun obtainBitmap(@DrawableRes drawableID: Int): Bitmap
    {
        val options = BitmapFactory.Options()
        options.inScaled = false
        return BitmapFactory.decodeResource(this.resources, drawableID, options)
    }

    /**
     * Obtain bitmap from drawable resources and resize it to fit the requested dimensions
     */
    fun obtainBitmap(@DrawableRes drawableID: Int, width: Int, height: Int): Bitmap
    {
        val bitmap = this.obtainBitmap(drawableID)

        if (bitmap.width != width || bitmap.height != height)
        {
            val scaled = Bitmap.createScaledBitmap(bitmap, width, height, false)
            bitmap.recycle()
            return scaled
        }

        return bitmap
    }

    /**
     * Obtain bitmap from assets
     */
    fun obtainBitmap(assetPath: String): Bitmap
    {
        val options = BitmapFactory.Options()
        options.inScaled = false
        val inputStream = this.assetManager.open(assetPath)
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.close()
        return bitmap ?: this.defaultBitmap()
    }

    /**
     * Obtain bitmap from assets and resize it to fit specified size
     */
    fun obtainBitmap(assetPath: String, width: Int, height: Int): Bitmap
    {
        val bitmap = this.obtainBitmap(assetPath)

        if (bitmap.width != width || bitmap.height != height)
        {
            val scaled = Bitmap.createScaledBitmap(bitmap, width, height, false)
            bitmap.recycle()
            return scaled
        }

        return bitmap
    }
}
